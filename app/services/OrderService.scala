package services
import com.google.inject.Inject
import daos.OrderDAO
import models.Order
import org.mongodb.scala.Completed
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

/**
  * Created by leandrobauret on 3/11/16.
  */


class OrderService @Inject()(orderDAO: OrderDAO) {
  def all(): Future[Seq[Order]] = orderDAO.all()

  def find(orderId: String): Future[Order] = orderDAO.find(orderId)

  def update(order: Order): Future[UpdateResult] = orderDAO.update(order)

  def save(order: Order): Future[Completed] = orderDAO.save(order)

  def drop(order: Order) : Future[Order] = orderDAO.drop(order)

}
