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

@ImplementedBy(classOf[MongoTestDAO])
trait TestDAO {
  def all(): Future[Seq[Test]]

  def find(testId: String): Future[Test]

  def update(test: Test): Future[UpdateResult]

  def save(test: Test): Future[Completed]

  def drop(test: Test) : Future[Test]

}

@Singleton
class MongoTestDAO @Inject()(mongo: Mongo) extends TestDAO {
  private val tests: MongoCollection[Document] = mongo.db.getCollection("test")

  override def all(): Future[Seq[Test]] = {
    tests.find().toFuture().map(doc => doc.map(documentToTest))
  }

  override def find(testId: String): Future[Test] = {
    tests.find(equal("_id", testId)).head().map[Test]((doc: Document) => {
      documentToTest(doc)
    })
  }

  override def update(test: Test): Future[UpdateResult] = {
    tests.replaceOne(equal("_id", test._id), Document(Json.toJson(test).toString)).head()
  }

  override def save(test: Test): Future[Completed] = {
    val testJson: String = Json.toJson(test).toString
    val doc: Document = Document(testJson)
    tests.insertOne(doc).head()
  }

  override def drop(testId: Test) : Future[Test] = {
    tests.findOneAndDelete(equal("_id", testId)).head().map[Test]((doc: Document) => {
      documentToTest(doc)
    })
  }

  private def documentToTest(doc: Document): Test = {
    Test(
      doc.get("_id").get.asString().getValue,
      doc.get("firstName").get.asString().getValue,
      doc.get("lastName").get.asString().getValue
    )
  }
}


