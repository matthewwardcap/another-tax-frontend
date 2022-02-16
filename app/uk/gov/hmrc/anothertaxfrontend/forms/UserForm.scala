/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
