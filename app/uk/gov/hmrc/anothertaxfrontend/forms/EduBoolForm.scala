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

case class EduBoolData(
                 education: Boolean
               )

object EduBoolForm {
  val form: Form[EduBoolData] = Form(mapping(
    "education" -> optional(text)
      .verifying("form.error.bool.blank", value => value.isDefined)
      .transform[Boolean](_.get.toBoolean, value => Some(value.toString))
  )(EduBoolData.apply)(EduBoolData.unapply)
  )
}
