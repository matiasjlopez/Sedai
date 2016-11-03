package models

/**
  * Created by matias on 02/11/2016.
  */
case class Bowl( _id: String,
                 name: String,
                 price: Double,
                 ingredients: List[Ingredient]
               )

object Bowl {

  import play.api.libs.json.Json

  implicit val BowlFormat = Json.format[Bowl]
}