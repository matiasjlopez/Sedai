package services
import com.google.inject.Inject
import daos.ContactDAO
import models.Contact
import org.mongodb.scala.Completed
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

/**
  * Created by leandrobauret on 3/11/16.
  */


class ContactService @Inject()(contactDAO: ContactDAO) {
  def all(): Future[Seq[Contact]] = contactDAO.all()

  def find(contactId: String): Future[Contact] = contactDAO.find(contactId)

  def update(contact: Contact): Future[UpdateResult] = contactDAO.update(contact)

  def save(contact: Contact): Future[Completed] = contactDAO.save(contact)

  def drop(contact: Contact) : Future[Contact] = contactDAO.drop(contact)

}
