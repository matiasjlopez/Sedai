package controllers

import java.util.UUID
import javax.inject._
import models.Test
import play.api._
import play.api.mvc._
import services.TestService

@Singleton
class TestController @Inject()(testService: TestService) extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def saveTest = Action {
    var test = Test(UUID.randomUUID.toString, "Matias", "Lopez")
    testService.save(test)
    Ok(views.html.index("Your new application is ready!"))
  }

}
