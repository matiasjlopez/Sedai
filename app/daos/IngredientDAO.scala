package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}
import models._
import org.bson.BsonValue
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import services.Mongo

import scala.concurrent.Future

@ImplementedBy(classOf[MongoIngredientDAO])
trait IngredientDAO {
  def all(): Future[Seq[Ingredient]]

  def find(ingredient: String): Future[Ingredient]

  def update(ingredient: Ingredient): Future[UpdateResult]

  def save(ingredient: Ingredient): Future[Completed]

  def drop(ingredient: Ingredient) : Future[Ingredient]

}

@Singleton
class MongoIngredientDAO @Inject()(mongo: Mongo) extends IngredientDAO {
  private val ingredients: MongoCollection[Document] = mongo.db.getCollection("ingredient")

  override def all(): Future[Seq[Ingredient]] = {
    ingredients.find().toFuture().map(doc => doc.map(documentToIngredient))
  }

  override def find(ingredientId: String): Future[Ingredient] = {
    ingredients.find(equal("_id", ingredientId)).head().map[Ingredient]((doc: Document) => {
      documentToIngredient(doc)
    })
  }

  override def update(ingredient: Ingredient): Future[UpdateResult] = {
    ingredients.replaceOne(equal("_id", ingredient._id), Document(Json.toJson(ingredient).toString)).head()
  }

  override def save(ingredient: Ingredient): Future[Completed] = {
    val ingredientJson: String = Json.toJson(ingredient).toString
    val doc: Document = Document(ingredientJson)
    ingredients.insertOne(doc).head()
  }

  override def drop(ingredient: Ingredient) : Future[Ingredient] = {
    ingredients.findOneAndDelete(equal("_id", ingredient._id)).head().map[Ingredient]((doc: Document) => {
      documentToIngredient(doc)
    })
  }

  private def documentToIngredient(doc: Document): Ingredient = {

    var bsonPrice: BsonValue = doc.get("price").get
    val price: Double = if(bsonPrice.toString contains "Double") bsonPrice.asDouble().getValue else bsonPrice.asInt32().doubleValue()

    Ingredient(
      doc.get("_id").get.asString().getValue,
      doc.get("name").get.asString().getValue,
      price
    )
  }
}
