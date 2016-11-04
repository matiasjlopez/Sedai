package controllers

import javax.inject._

import models.{Order, Purchase}
import play.api.mvc._
import services.{OrderService, PurchaseService}

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration


@Singleton
class CashRegisterController @Inject()(orderService: OrderService, purchaseService: PurchaseService) extends Controller{

  def showCashRegister = Action {
    //Get all Purchases from DB.
    var purchases = Seq[Purchase]()
    val purchasesFromDB: Future[Seq[Purchase]] = purchaseService.all()
    purchases = Await.result(purchasesFromDB, Duration.Inf)

    //Get all Orders from DB.
    var orders = Seq[Order]()
    val ordersFromDB: Future[Seq[Order]] = orderService.all()
    orders = Await.result(ordersFromDB, Duration.Inf)

    //Send purchases and orders to index...

    Ok(views.html.index("Your new application is ready."))
  }
}
