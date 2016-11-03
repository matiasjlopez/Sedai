package models

/**
  * Created by matias on 02/11/2016.
  */
case class Ingredient( _id: String,
                 name: String,
                 price: Double
               )

object Ingredient {

  import play.api.libs.json.Json

  implicit val IngredientFormat = Json.format[Ingredient]
}