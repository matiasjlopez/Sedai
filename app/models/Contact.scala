package models

/**
  * Created by leandrobauret on 2/11/16.
  */
case class Contact( _id: String,
                    name: String,
                    phone: String
                )

object Contact{

  import play.api.libs.json.Json

  implicit val ContactFormat = Json.format[Contact]
}
