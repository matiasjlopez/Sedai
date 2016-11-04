package controllers

import java.util.UUID
import javax.inject._

import models._
import play.api.mvc._
import services.{BowlService, OrderService}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

/**
  * Created by leandrobauret on 3/11/16.
  */

@Singleton
class OrderController @Inject()(orderService: OrderService, bowlService: BowlService) extends Controller{

  //Test saving order - This method will receive as parameters all variables (created for testing)
  def saveOrder = Action {

    val contact: Contact = new Contact(UUID.randomUUID().toString,"Leandro Bauret","1531919370") //Creating contact

    //Creating new Bowl
    val ingredientsList: List[Ingredient] = List(Ingredient(UUID.randomUUID().toString, "Kanikama", 30.0),Ingredient(UUID.randomUUID().toString, "Salsa Soja", 15.0))
    var bowlPrice: Double = 0.0
    for(ingredient <- ingredientsList){
      bowlPrice = bowlPrice + ingredient.price
    }
    val newBowl: Bowl = Bowl(UUID.randomUUID().toString, "KanikamaBowl", bowlPrice, ingredientsList)

    //Fetching Bowl from DB after running BowlController.save()
    val bowlFromDB: Future[Bowl] = bowlService.find("30b8a026-9bc2-40f4-a83f-a3a26f928d01")
    val fetchedBowl = Await.result(bowlFromDB,Duration.Inf)

    //List of ordered bowls
    val bowls:List[Bowl] = List(fetchedBowl, newBowl)

    //Contact address
    val address: Address = new Address(UUID.randomUUID().toString,551,"Olaguer y Feliu","Martinez","San Isidro", "551")

    //Calculate Order's final price
    var orderPrice: Double = 0.0
    for(bowl <- bowls){
      orderPrice +=  bowl.price
    }

    orderService.save(Order(UUID.randomUUID().toString,contact,bowls,"Status2","04/11/2016",address,orderPrice))

    Ok(views.html.index("Your new application is ready."))
  }
}
