package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}
import models._
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import services.Mongo

import scala.concurrent.Future

@ImplementedBy(classOf[MongoPurchaseDAO])
trait PurchaseDAO {
  def all(): Future[Seq[Purchase]]

  def find(purchase: String): Future[Purchase]

  def update(purchase: Purchase): Future[UpdateResult]

  def save(purchase: Purchase): Future[Completed]

  def drop(purchase: Purchase) : Future[Purchase]

}

@Singleton
class MongoPurchaseDAO @Inject()(mongo: Mongo) extends PurchaseDAO {
  private val purchases: MongoCollection[Document] = mongo.db.getCollection("purchase")

  override def all(): Future[Seq[Purchase]] = {
    purchases.find().toFuture().map(doc => doc.map(documentToPurchase))
  }

  override def find(purchaseId: String): Future[Purchase] = {
    purchases.find(equal("_id", purchaseId)).head().map[Purchase]((doc: Document) => {
      documentToPurchase(doc)
    })
  }

  override def update(purchase: Purchase): Future[UpdateResult] = {
    purchases.replaceOne(equal("_id", purchase._id), Document(Json.toJson(purchase).toString)).head()
  }

  override def save(purchase: Purchase): Future[Completed] = {
    val purchaseJson: String = Json.toJson(purchase).toString
    val doc: Document = Document(purchaseJson)
    purchases.insertOne(doc).head()
  }

  override def drop(purchase: Purchase) : Future[Purchase] = {
    purchases.findOneAndDelete(equal("_id", purchase._id)).head().map[Purchase]((doc: Document) => {
      documentToPurchase(doc)
    })
  }

  private def documentToPurchase(doc: Document): Purchase = {

    Purchase(
      doc.get("_id").get.asString().getValue,
      doc.get("ingredient").get.asInstanceOf[Ingredient],
      doc.get("quantity").get.asInt32().getValue,
      doc.get("cost").get.asInt32().getValue,
      doc.get("date").get.asString().getValue
    )
  }
}
