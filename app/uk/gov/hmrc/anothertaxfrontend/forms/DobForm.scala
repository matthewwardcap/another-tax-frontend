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
import scala.util.Try

case class DobData(
                 day:Int,
                 month:Int,
                 year:Int
               )

object DobForm {
  val form: Form[DobData] = Form(mapping(
    "day" -> text
      .verifying("Enter a Day", value => value.nonEmpty)
      .verifying("Day must be a number", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("Day must be above 0", i => i > 0)
      .verifying("Day must be be 31 or below", i => i < 32),
    "month" -> text
      .verifying("Enter a Month", value => value.nonEmpty)
      .verifying("Month must be a number", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("Month must be above 0", i => i > 0)
      .verifying("Month must be 12 or below", i => i < 13),
    "year" -> text
      .verifying("Enter a Year", value => value.nonEmpty)
      .verifying("Year must be a number", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("Year must be above 1850", i => i > 1850)
  )(DobData.apply)(DobData.unapply)
    .verifying("Date of birth can't be in the future", model => !LocalDate.of(model.year, model.month, model.day).isAfter(LocalDate.now))
  )
}
