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

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, YearMonth}
import scala.util.Try

case class EduDateData(
                    day:Int,
                    month:Int,
                    year:Int
                  )

object EduDateForm {
  val form: Form[EduDateData] = Form(mapping(
    "day" -> text
      .verifying("form.error.edu.day.blank", value => value.nonEmpty)
      .verifying("form.error.edu.day.char", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("form.error.edu.day.low", i => i > 0)
      .verifying("form.error.edu.day.high", i => i < 32),
    "month" -> text
      .verifying("form.error.edu.month.blank", value => value.nonEmpty)
      .verifying("form.error.edu.month.char", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("form.error.edu.month.low", i => i > 0)
      .verifying("form.error.edu.month.high", i => i < 13),
    "year" -> text
      .verifying("form.error.edu.year.blank", value => value.nonEmpty)
      .verifying("form.error.edu.year.char", i => Try(i.toInt).isSuccess || i.isEmpty)
      .transform[Int](_.toInt, _.toString)
      .verifying("form.error.edu.year.low", i => i > 1850)
  )(EduDateData.apply)(EduDateData.unapply)
    .verifying("form.error.edu.date.day", model => model.day < YearMonth.from(LocalDate.of(model.year, model.month, 1)).atEndOfMonth().getDayOfMonth)
    .verifying("form.error.edu.date.future", model => !LocalDate.parse(model.year.toString+"-"+model.month.toString+"-"+model.day.toString, DateTimeFormatter.ofPattern("yyyy-M-d")).isAfter(LocalDate.now))
  )
}
