package models

/**
  * Created by leandrobauret on 2/11/16.
  */
case class Address( _id: String,
                    streetNumber: Int,
                    street: String,
                    district: String,
                    neighbourhood: String,
                    lote: String
                )

object Address{

  import play.api.libs.json.Json

  implicit val AddressFormat = Json.format[Address]
}
