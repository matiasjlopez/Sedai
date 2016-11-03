package models

/**
  * Created by leandrobauret on 2/11/16.
  */

case class Order( _id: String,
                   contact: Contact,
                   bowls: List[Bowl],
                   status: String,
                   date: String,
                   address: Address,
                   price: Double
                 )

object Order{

  import play.api.libs.json.Json

  implicit val OrderFormat = Json.format[Order]
}


