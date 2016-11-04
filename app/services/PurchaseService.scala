package services
import com.google.inject.Inject
import daos.PurchaseDAO
import models.Purchase
import org.mongodb.scala.Completed
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

/**
  * Created by leandrobauret on 3/11/16.
  */


class PurchaseService @Inject()(purchaseDAO: PurchaseDAO) {
  def all(): Future[Seq[Purchase]] = purchaseDAO.all()

  def find(purchaseId: String): Future[Purchase] = purchaseDAO.find(purchaseId)

  def update(purchase: Purchase): Future[UpdateResult] = purchaseDAO.update(purchase)

  def save(purchase: Purchase): Future[Completed] = purchaseDAO.save(purchase)

  def drop(purchase: Purchase) : Future[Purchase] = purchaseDAO.drop(purchase)

}
