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

class NameFormSpec extends UnitSpec {
  val form: Form[Data] = NameForm.form

  "NameForm.form" when {

    "no first name is supplied" must {
      "result in a an error against the first name field" in {
        val result = form.bind(Map("lastName" -> "Smith"))
        val error = result("firstName").error.getOrElse(fail("error against first name field not generated"))
        error.message mustBe "error.required"
      }
    }

    "no last name is supplied" must {
      "result in a an error against the last name field" in {
        val result = form.bind(Map("firstName" -> "John"))
        val error = result("lastName").error.getOrElse(fail("error against last name field not generated"))
        error.message mustBe "error.required"
      }
    }

    "first name is blank" must {
      "result in a an error against the first name field" in {
        val result = form.bind(Map("firstName" -> "", "lastName" -> "Smith"))
        val error = result("firstName").error.getOrElse(fail("error against first name field not generated"))
        error.message mustBe "Enter your first name"
      }
    }

    "last name is blank" must {
      "result in a an error against the last name field" in {
        val result = form.bind(Map("firstName" -> "John", "lastName" -> ""))
        val error = result("lastName").error.getOrElse(fail("error against last name field not generated"))
        error.message mustBe "Enter your last name"
      }
    }

    "first name is number" must {
      "result in a an error against the first name field" in {
        val result = form.bind(Map("firstName" -> "1234"))
        val error = result("firstName").error.getOrElse(fail("error against first name field not generated"))
        error.message mustBe "First name can't contain number"
      }
    }

    "middle name is number" must {
      "result in a an error against the middle name field" in {
        val result = form.bind(Map("middleName" -> "1234"))
        val error = result("middleName").error.getOrElse(fail("error against middle name field not generated"))
        error.message mustBe "Middle name can't contain number"
      }
    }

    "last name is number" must {
      "result in a an error against the last name field" in {
        val result = form.bind(Map("lastName" -> "1234"))
        val error = result("lastName").error.getOrElse(fail("error against last name field not generated"))
        error.message mustBe "Last name can't contain number"
      }
    }

    "first name contains number" must {
      "result in a an error against the first name field" in {
        val result = form.bind(Map("firstName" -> "test1234"))
        val error = result("firstName").error.getOrElse(fail("error against first name field not generated"))
        error.message mustBe "First name can't contain number"
      }
    }

    "middle name contains number" must {
      "result in a an error against the middle name field" in {
        val result = form.bind(Map("middleName" -> "test1234"))
        val error = result("middleName").error.getOrElse(fail("error against middle name field not generated"))
        error.message mustBe "Middle name can't contain number"
      }
    }

    "last name contains number" must {
      "result in a an error against the last name field" in {
        val result = form.bind(Map("lastName" -> "test1234"))
        val error = result("lastName").error.getOrElse(fail("error against last name field not generated"))
        error.message mustBe "Last name can't contain number"
      }
    }

    "bound with data containing a valid name without a middle name" must {

      val model = Data("John", None, "Smith")
      val data = Map("firstName" -> model.firstName, "lastName" -> model.lastName)

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a FullName model with no middle name" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(model)
        }
      }
    }

    "bound with data containing a valid name with a middle name" must {

      val model = Data("John", Some("Middle"), "Smith")
      val data = Map(
        "firstName" -> model.firstName,
        "middleName" -> model.middleName.get,
        "lastName" -> model.lastName
      )

      "result in no errors" in {
        val result = form.bind(data)
        val errors = result.hasErrors
        errors mustBe false
      }

      "generate a FullName model with a middle name" in {
        val result = form.bind(data)
        if (result.hasErrors) {
          fail("errors reported on a valid form")
        } else {
          result.value mustBe Some(model)
        }
      }
    }

    "built with a model containing a middle name" must {

      val model = Data("John", Some("Middle"), "Smith")
      val data = Map(
        "firstName" -> model.firstName,
        "middleName" -> model.middleName.get,
        "lastName" -> model.lastName
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

    "built with a model containing no middle name" must {

      val model = Data("John", None, "Smith")
      val data = Map(
        "firstName" -> model.firstName,
        "lastName" -> model.lastName
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
