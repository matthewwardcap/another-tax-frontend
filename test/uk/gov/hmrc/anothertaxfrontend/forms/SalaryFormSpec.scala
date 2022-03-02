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

class SalaryFormSpec extends UnitSpec {
  val form: Form[SalaryData] = SalaryForm.form

  "SalaryForm.form" when {

    "no salary is supplied" must {
      "result in a an error against the salary field" in {
        val result = form.bind(Map("" -> ""))
        val error = result("salary").error.getOrElse(fail("error against salary field not generated"))
        error.message mustBe "error.required"
      }
    }

    "salary is below 0" must {
      "result in a an error against the salary field" in {
        val result = form.bind(Map("salary" -> "-1"))
        val error = result("").error.getOrElse(fail("error against salary field not generated"))
        error.message mustBe "Salary can't be below £0"
      }
    }

    "salary has more than 2 decimal places" must {
      "result in a an error against the salary field" in {
        val result = form.bind(Map("salary"-> "30000.666"))
        val error = result("salary").error.getOrElse(fail("error against salary field not generated"))
        error.message mustBe "error.real.precision"
      }
    }

    "salary has more than 32 length" must {
      "result in a an error against the salary field" in {
        val result = form.bind(Map("salary"-> "30000000000000000000000000000000000000000"))
        val error = result("salary").error.getOrElse(fail("error against salary field not generated"))
        error.message mustBe "error.real.precision"
      }
    }

    "bound with data containing a valid salary" must {

      val model = SalaryData(30000)
      val data = Map("salary" -> model.salary.toString)

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a SalaryData model" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(model)
        }
      }
    }

    "built with a model with a valid salary" must {

      val model = SalaryData(30000)
      val data = Map(
        "salary" -> String.format("%.2f", model.salary.bigDecimal)
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
