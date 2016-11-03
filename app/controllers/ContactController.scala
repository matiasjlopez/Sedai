package controllers
import javax.inject._

import play.api.mvc._
import services.ContactService

/**
  * Created by leandrobauret on 3/11/16.
  */

@Singleton
class ContactController @Inject()(contactService: ContactService) extends Controller{

  def saveContact = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}
