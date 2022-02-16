package uk.gov.hmrc.anothertaxfrontend.forms
import play.api.data.Forms._
import play.api.data.Form
import play.api.data.validation.Constraints._
import uk.gov.hmrc.anothertaxfrontend.models.User
import uk.gov.hmrc.anothertaxfrontend.models.User._

object UserForm {
  val form: Form[User] = Form(mapping(
    firstName -> nonEmptyText,
    middleName -> nonEmptyText,
    lastName -> nonEmptyText,
    dob -> date,
    education -> boolean,
    educationDate -> date,
    employmentStatus -> nonEmptyText,
    salary -> bigDecimal
  )(User.apply)(User.unapply))
}
