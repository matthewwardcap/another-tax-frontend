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

case class SalaryData(
                       salary:BigDecimal,
                  )

object SalaryForm {
  val form: Form[SalaryData] = Form(mapping(
    "salary" -> bigDecimal(32,2),
  )(SalaryData.apply)(SalaryData.unapply)
    .verifying("form.error.salary.low", model => model.salary >= 0)
  )
}
