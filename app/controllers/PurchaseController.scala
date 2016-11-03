package controllers
import javax.inject._

import play.api.mvc._
import services.PurchaseService

/**
  * Created by leandrobauret on 3/11/16.
  */

@Singleton
class PurchaseController @Inject()(purchaseService: PurchaseService) extends Controller{

  def savePurchase = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
