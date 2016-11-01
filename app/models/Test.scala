package models

/**
  * Created by matias on 01/11/2016.
  */
case class Test(_id: String,
                firstName: String,
                lastName: String
               )

object Test {

    import play.api.libs.json.Json

    implicit val TestFormat = Json.format[Test]

}
