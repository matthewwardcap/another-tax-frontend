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

class DobFormSpec extends UnitSpec {
  val form: Form[DobData] = DobForm.form

  "DobForm.form" when {

    "no day is supplied" must {
      "result in a an error against the day field" in {
        val result = form.bind(Map("month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "error.required"
      }
    }

    "no month is supplied" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "error.required"
      }
    }

    "no year is supplied" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3"))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "error.required"
      }
    }

    "date is in the future" must {
      "result in a an error" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> "2100"))
        val error = result("").error.getOrElse(fail("error against form not generated"))
        error.message mustBe "Date of birth can't be in the future"
      }
    }

    "year is before 1850" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> "1800"))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "error.min"
      }
    }

    "day is below 1" must {
      "result in a an error against the day field" in {
        val result = form.bind(Map("day" -> "0", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "error.min"
      }
    }

    "month is below 1" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "0", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "error.min"
      }
    }

    "day is above 31" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "32", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "error.max"
      }
    }

    "month is above 12" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "13", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "error.max"
      }
    }

    "bound with data containing a valid date" must {

      val model = DobData(19, 3, 2000)
      val data = Map("day" -> model.day.toString, "month" -> model.month.toString, "year" -> model.year.toString)

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a DobData model" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(model)
        }
      }
    }

    "built with a model with a valid date" must {

      val model = DobData(19, 3, 2000)
      val data = Map(
        "day" -> model.day.toString,
        "month" -> model.month.toString,
        "year" -> model.year.toString
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
