package controllers

import javax.inject._

import play.api.mvc._
import services.{IngredientService, BowlService}


@Singleton
class IngredientController @Inject()(ingredientService: IngredientService) extends Controller {

  def saveIngredient = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
