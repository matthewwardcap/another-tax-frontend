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

package uk.gov.hmrc.anothertaxfrontend.controllers

import play.api.Application
import play.api.Play.materializer
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.anothertaxfrontend.models.User
import play.api.test.CSRFTokenHelper._

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DobControllerSpec extends ControllerSpecBase {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val controller = inject[DobController]

  "DobController" when {
    "calling show()" must {
      "return 200 (Ok) if user exists and previous fields done" in {
        val user = User(Some("first"), Some("middle"), Some("first"), None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
      }
      "return HTML if user exists and previous fields done" in {
        val user = User(Some("first"), Some("middle"), Some("first"), None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob").withSession("user" -> Json.toJson(user.copy()).toString))
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      "return 200 (Ok) if user exists and field already filled" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
        contentAsString(result) must include ("19")
        contentAsString(result) must include ("3")
        contentAsString(result) must include ("2000")
      }
      "return 303 if user doesn't exist" in {
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob").withSession())
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to name if name field missing" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/dob")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/name")
      }
    }

    "calling post()" must {
      "return 303 (Redirect) if user exists and form filled correctly" in {
        val user = User(Some("first"), Some("middle"), Some("first"), None, None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/dob-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("day" -> "19", "month" -> "3", "year" -> "2000").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/edu-bool")
      }
      "return 303 and redirect to home if user doesn't exist" in {
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/dob-post"))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 400 (BAD_REQUEST) and refresh if form has error" in {
        val user = User(Some("first"), Some("middle"), Some("first"), None, None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/dob-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("day" -> "", "month" -> "", "year" -> "").withCSRFToken
        )
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include ("Numeric value expected")
      }
      "return 303 and redirect to summary if summary true" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), None, Some(false), None, Some("Unemployed"), None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/dob-post")
          .withSession("user" -> Json.toJson(user.copy()).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("day" -> "19", "month" -> "3", "year" -> "2000").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/summary")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/dob-post")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("day" -> "19", "month" -> "3", "year" -> "2000").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to name if name field missing" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(POST, "/another-tax-service/dob-post")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("day" -> "19", "month" -> "3", "year" -> "2000").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/name")
      }
    }

    "calling back()" must {
      "return 303 (Redirect)" in {
        val user = User(Some("first"), Some("middle"), Some("first"), None, None, None, None, None)
        val result = controller.back()(FakeRequest(POST, "/another-tax-service/dob-back")
          .withSession("user" -> Json.toJson(user.copy()).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/name")
      }
    }

  }

}