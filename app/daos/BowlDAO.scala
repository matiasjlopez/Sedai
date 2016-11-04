package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}
import models._
import org.bson.{BsonArray, BsonValue}
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import services.Mongo

import collection.JavaConversions._
import scala.concurrent.Future

@ImplementedBy(classOf[MongoBowlDAO])
trait BowlDAO {
  def all(): Future[Seq[Bowl]]

  def find(bowlId: String): Future[Bowl]

  def update(bowl: Bowl): Future[UpdateResult]

  def save(bowl: Bowl): Future[Completed]

  def drop(bowl: Bowl) : Future[Bowl]

}

@Singleton
class MongoBowlDAO @Inject()(mongo: Mongo) extends BowlDAO {
  private val bowls: MongoCollection[Document] = mongo.db.getCollection("bowl")

  override def all(): Future[Seq[Bowl]] = {
    bowls.find().toFuture().map(doc => doc.map(documentToBowl))
  }

  override def find(bowlId: String): Future[Bowl] = {
    bowls.find(equal("_id", bowlId)).head().map[Bowl]((doc: Document) => {
      documentToBowl(doc)
    })
  }

  override def update(bowl: Bowl): Future[UpdateResult] = {
    bowls.replaceOne(equal("_id", bowl._id), Document(Json.toJson(bowl).toString)).head()
  }

  override def save(bowl: Bowl): Future[Completed] = {
    val bowlJson: String = Json.toJson(bowl).toString
    val doc: Document = Document(bowlJson)
    bowls.insertOne(doc).head()
  }

  override def drop(bowl: Bowl) : Future[Bowl] = {
    bowls.findOneAndDelete(equal("_id", bowl._id)).head().map[Bowl]((doc: Document) => {
      documentToBowl(doc)
    })
  }

  private def documentToBowl(doc: Document): Bowl = {
    var ingredients: List[Ingredient] = List[Ingredient]()

    try{
      ingredients = bsonToListIngredient(doc.get("ingredients").get.asArray())
    } catch {
      case _ => {
        println("Error: The bowl don't have a list of ingredients.")
      }
    }

    var bsonPrice: BsonValue = doc.get("price").get
    val price: Double = if(bsonPrice.toString contains "Double") bsonPrice.asDouble().getValue else bsonPrice.asInt32().doubleValue()

    Bowl(
      doc.get("_id").get.asString().getValue,
      doc.get("name").get.asString().getValue,
      price,
      ingredients
    )
  }

  private def bsonToListIngredient(bson : BsonArray) : List[Ingredient] ={
    var ingredients = List[Ingredient]()
    for(bsonV : BsonValue <- bson.getValues){
      var doc = bsonV.asDocument()
      ingredients = ingredients :+ Ingredient(doc.get("_id").asString().getValue,doc.get("name").asString().getValue,doc.get("price").asDouble().getValue)
    }
    ingredients
  }

}
