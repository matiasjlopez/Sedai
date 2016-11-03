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

@ImplementedBy(classOf[MongoContactDAO])
trait ContactDAO {
  def all(): Future[Seq[Contact]]

  def find(contact: String): Future[Contact]

  def update(contact: Contact): Future[UpdateResult]

  def save(contact: Contact): Future[Completed]

  def drop(contact: Contact) : Future[Contact]

}

@Singleton
class MongoContactDAO @Inject()(mongo: Mongo) extends ContactDAO {
  private val contacts: MongoCollection[Document] = mongo.db.getCollection("contact")

  override def all(): Future[Seq[Contact]] = {
    contacts.find().toFuture().map(doc => doc.map(documentToContact))
  }

  override def find(contactId: String): Future[Contact] = {
    contacts.find(equal("_id", contactId)).head().map[Contact]((doc: Document) => {
      documentToContact(doc)
    })
  }

  override def update(contact: Contact): Future[UpdateResult] = {
    contacts.replaceOne(equal("_id", contact._id), Document(Json.toJson(contact).toString)).head()
  }

  override def save(contact: Contact): Future[Completed] = {
    val contactJson: String = Json.toJson(contact).toString
    val doc: Document = Document(contactJson)
    contacts.insertOne(doc).head()
  }

  override def drop(contact: Contact) : Future[Contact] = {
    contacts.findOneAndDelete(equal("_id", contact._id)).head().map[Contact]((doc: Document) => {
      documentToContact(doc)
    })
  }

  private def documentToContact(doc: Document): Contact = {

    Contact(
      doc.get("_id").get.asString().getValue,
      doc.get("name").get.asString().getValue,
      doc.get("phone").get.asString().getValue
    )
  }
}
