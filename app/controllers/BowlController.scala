package controllers

import javax.inject._

import play.api.mvc._
import services.BowlService


@Singleton
class BowlController @Inject()(bowlService: BowlService) extends Controller{

  def saveBowl = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
