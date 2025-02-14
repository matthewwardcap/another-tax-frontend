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
        error.message mustBe "form.error.date.future"
      }
    }

    "year is before 1850" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> "1800"))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "form.error.year.low"
      }
    }

    "day is not in month" must {
      "result in a an error against the entire form" in {
        val result = form.bind(Map("day" -> "31", "month" -> "2", "year" -> "2000"))
        val error = result("").error.getOrElse(fail("error against form not generated"))
        error.message mustBe "form.error.date.day"
      }
    }

    "day is below 1" must {
      "result in a an error against the day field" in {
        val result = form.bind(Map("day" -> "0", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "form.error.day.low"
      }
    }

    "month is below 1" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "0", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "form.error.month.low"
      }
    }

    "day is above 31" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "32", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "form.error.day.high"
      }
    }

    "month is above 12" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "13", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "form.error.month.high"
      }
    }

    "day blank" must {
      "result in a an error against the day field" in {
        val result = form.bind(Map("day" -> "", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "form.error.day.blank"
      }
    }

    "month blank" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "form.error.month.blank"
      }
    }

    "year blank" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> ""))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "form.error.year.blank"
      }
    }

    "day not number" must {
      "result in a an error against the day field" in {
        val result = form.bind(Map("day" -> "a", "month" -> "3", "year" -> "2000"))
        val error = result("day").error.getOrElse(fail("error against day field not generated"))
        error.message mustBe "form.error.day.char"
      }
    }

    "month not number" must {
      "result in a an error against the month field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "a", "year" -> "2000"))
        val error = result("month").error.getOrElse(fail("error against month field not generated"))
        error.message mustBe "form.error.month.char"
      }
    }

    "year not number" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> "a"))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "form.error.year.char"
      }
    }

    "year more than 4 characters" must {
      "result in a an error against the year field" in {
        val result = form.bind(Map("day" -> "19", "month" -> "3", "year" -> "20000"))
        val error = result("year").error.getOrElse(fail("error against year field not generated"))
        error.message mustBe "form.error.year.length"
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
