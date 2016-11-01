package services

import java.text.Normalizer

import com.google.inject.Inject
import daos.TestDAO
import models.Test
import org.mongodb.scala.Completed
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

/**
  * Created by Fede on 8/28/2016.
  */
class TestService @Inject()(testDao: TestDAO) {
  def all(): Future[Seq[Test]] = testDao.all()

  def find(testId: String): Future[Test] = testDao.find(testId)

  def update(test: Test): Future[UpdateResult] = testDao.update(test.copy(firstName = Normalizer.normalize(test.firstName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")))

  def save(test: Test): Future[Completed] = testDao.save(test)

  def drop(test: Test) : Future[Test] = testDao.drop(test)
}
