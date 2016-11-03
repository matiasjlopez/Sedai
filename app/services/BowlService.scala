package services

import com.google.inject.Inject
import daos.BowlDAO
import models.Bowl
import org.mongodb.scala.Completed
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

class BowlService @Inject()(bowlDAO: BowlDAO) {
  def all(): Future[Seq[Bowl]] = bowlDAO.all()

  def find(bowlId: String): Future[Bowl] = bowlDAO.find(bowlId)

  def update(bowl: Bowl): Future[UpdateResult] = bowlDAO.update(bowl)

  def save(bowl: Bowl): Future[Completed] = bowlDAO.save(bowl)

  def drop(bowl: Bowl) : Future[Bowl] = bowlDAO.drop(bowl)

}
