package controllers
import javax.inject._

import play.api.mvc._
import services.OrderService

/**
  * Created by leandrobauret on 3/11/16.
  */

@Singleton
class OrderController @Inject()(orderService: OrderService) extends Controller{

  def saveOrder = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
