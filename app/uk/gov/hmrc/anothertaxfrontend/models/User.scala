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

package uk.gov.hmrc.anothertaxfrontend.models

import java.util.Date
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class User(
                 firstName:Option[String],
                 middleName:Option[String],
                 lastName:Option[String],
                 dob:Option[Date],
                 education:Option[Boolean],
                 educationDate:Option[Date],
                 employmentStatus:Option[String],
                 salary:Option[BigDecimal]
               )


object User {
  //implicit val userReads: Reads[User] = Json.reads[User]
  //implicit val userWrites: OWrites[User] = Json.writes[User]
  implicit val format: OFormat[User] = Json.format[User]

  val firstName         = "firstName"
  val middleName        = "middleName"
  val lastName          = "lastName"
  val dob               = "dob"
  val education         = "education"
  val educationDate     = "educationDate"
  val employmentStatus  = "employmentStatus"
  val salary            = "salary"
}
