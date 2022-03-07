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

case class Data(
                 firstName:String,
                 middleName:Option[String],
                 lastName:String
               )

object NameForm {
  val form: Form[Data] = Form(mapping(
    "firstName" -> text
      .verifying("form.error.firstname.blank", value => value.trim.nonEmpty)
      .verifying("form.error.firstname.number", value => !value.exists(_.isDigit)),
    "middleName" -> optional(text).verifying("form.error.middlename.number", value => !value.getOrElse("").exists(_.isDigit) || value.isEmpty),
    "lastName" -> text
      .verifying("form.error.lastname.blank", value => value.trim.nonEmpty)
      .verifying("form.error.lastname.number", value => !value.exists(_.isDigit))
  )(Data.apply)(Data.unapply)
  )
}
