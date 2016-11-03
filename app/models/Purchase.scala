package models

/**
  * Created by leandrobauret on 2/11/16.
  */

case class Purchase( _id: String,
                     ingredient: Ingredient,
                     quantity: Int,
                     cost: Int,
                     date: String
                )

object Purchase{

  import play.api.libs.json.Json

  implicit val PurchaseFormat = Json.format[Purchase]
}
