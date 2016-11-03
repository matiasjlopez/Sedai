package services

import com.google.inject.Inject
import daos.IngredientDAO
import models.Ingredient
import org.mongodb.scala.Completed
import org.mongodb.scala.result._

import scala.concurrent.Future

class IngredientService @Inject()(ingredientDAO: IngredientDAO) {
  def all(): Future[Seq[Ingredient]] = ingredientDAO.all()

  def find(ingredientId: String): Future[Ingredient] = ingredientDAO.find(ingredientId)

  def update(ingredient: Ingredient): Future[UpdateResult] = ingredientDAO.update(ingredient)

  def save(ingredient: Ingredient): Future[Completed] = ingredientDAO.save(ingredient)

  def drop(ingredient: Ingredient) : Future[Ingredient] = ingredientDAO.drop(ingredient)

}
