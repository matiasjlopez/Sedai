package controllers

import java.util.UUID
import javax.inject._

import models.{Ingredient, Bowl}
import play.api.mvc._
import services.BowlService


@Singleton
class BowlController @Inject()(bowlService: BowlService) extends Controller{

  def saveBowl = Action {
    //En verdad este método va a recibir los id de los ingredientes y el nombre desde la view.
    //Entonces se buscan los ingredientes en la bd y se crea el bowl.
    val ingredientsList: List[Ingredient] = List(Ingredient(UUID.randomUUID().toString, "Salmon", 50.00),Ingredient(UUID.randomUUID().toString, "Arroz", 25.00))
    var bowlPrice: Double = 0.0
    for(ingredient <- ingredientsList){
      bowlPrice = bowlPrice + ingredient.price
    }
    val bowl: Bowl = Bowl(UUID.randomUUID().toString, "SalmonBowl", bowlPrice, ingredientsList)
    bowlService.save(bowl)

    Ok(views.html.index("Your new application is ready."))
  }
}
