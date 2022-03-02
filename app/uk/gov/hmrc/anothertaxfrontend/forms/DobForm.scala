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

import java.time.{LocalDate, Year}

case class DobData(
                 day:Int,
                 month:Int,
                 year:Int
               )

object DobForm {
  val form: Form[DobData] = Form(mapping(
    "day" -> number.verifying(min(1), max(31)),
    "month" -> number.verifying(min(1), max(12)),
    "year" -> number.verifying(min(1850))
  )(DobData.apply)(DobData.unapply)
    .verifying("Date of birth can't be in the future", model => !LocalDate.of(model.year, model.month, model.day).isAfter(LocalDate.now))
  )
}
