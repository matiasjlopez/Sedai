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

@ImplementedBy(classOf[MongoOrderDAO])
trait OrderDAO {
  def all(): Future[Seq[Order]]

  def find(orderId: String): Future[Order]

  def update(order: Order): Future[UpdateResult]

  def save(order: Order): Future[Completed]

  def drop(order: Order) : Future[Order]

}

@Singleton
class MongoOrderDAO @Inject()(mongo: Mongo) extends OrderDAO {
  private val orders: MongoCollection[Document] = mongo.db.getCollection("order")

  override def all(): Future[Seq[Order]] = {
    orders.find().toFuture().map(doc => doc.map(documentToOrder))
  }

  override def find(orderId: String): Future[Order] = {
    orders.find(equal("_id", orderId)).head().map[Order]((doc: Document) => {
      documentToOrder(doc)
    })
  }

  override def update(order: Order): Future[UpdateResult] = {
    orders.replaceOne(equal("_id", order._id), Document(Json.toJson(order).toString)).head()
  }

  override def save(order: Order): Future[Completed] = {
    val orderJson: String = Json.toJson(order).toString
    val doc: Document = Document(orderJson)
    orders.insertOne(doc).head()
  }

  override def drop(order: Order) : Future[Order] = {
    orders.findOneAndDelete(equal("_id", order._id)).head().map[Order]((doc: Document) => {
      documentToOrder(doc)
    })
  }

  private def documentToOrder(doc: Document): Order = {
    var bowls: List[Bowl] = List[Bowl]()

    try{
      bowls = bsonToListBowl(doc.get("bowls").get.asArray())
    } catch {
      case _ => {
        println("Error: The Order doesn't have a list of bowls.")
      }
    }

    var bsonPrice: BsonValue = doc.get("price").get
    val price: Double = if(bsonPrice.toString contains "Double") bsonPrice.asDouble().getValue else bsonPrice.asInt32().doubleValue()

    Order(
      doc.get("_id").get.asString().getValue,
      doc.get("contact").get.asInstanceOf[Contact],
      bowls,
      doc.get("status").get.asString().getValue,
      doc.get("date").get.asString().getValue,
      doc.get("address").get.asInstanceOf[Address],
      price
    )
  }

  private def bsonToListBowl(bson : BsonArray) : List[Bowl] ={
    var bowls = List[Bowl]()
    for(bsonV : BsonValue <- bson.getValues){
      var doc = bsonV.asDocument()
      bowls = bowls :+ Bowl(doc.get("_id").asString().getValue,doc.get("name").asString().getValue,doc.get("price").asDouble().getValue,doc.get("ingredients").asArray().getValues.asInstanceOf[List[Ingredient]])
    }
    bowls
  }

}
