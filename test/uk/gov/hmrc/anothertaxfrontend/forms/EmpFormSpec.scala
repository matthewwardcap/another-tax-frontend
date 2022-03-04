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

import play.api.data.Form
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

class EmpFormSpec extends UnitSpec {
  val form: Form[EmpData] = EmpForm.form

  "EmpForm.form" when {

    "no employmentStatus is supplied" must {
      "result in a an error against the employmentStatus field" in {
        val result = form.bind(Map("" -> ""))
        val error = result("employmentStatus").error.getOrElse(fail("error against employmentStatus field not generated"))
        error.message mustBe "Choose an employment option"
      }
    }

    "bound with data containing a valid employmentStatus" must {

      val model = EmpData("Unemployed")
      val data = Map("employmentStatus" -> model.employmentStatus)

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a EmpData model" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(model)
        }
      }
    }

    "built with a model" must {

      val model = EmpData("Unemployed")
      val data = Map(
        "employmentStatus" -> model.employmentStatus
      )

      "result in a form with no errors" in {
        val result = form.fillAndValidate(model)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a populated form" in {
        val result = form.fillAndValidate(model)
        result.data mustBe data
      }
    }

  }
}
