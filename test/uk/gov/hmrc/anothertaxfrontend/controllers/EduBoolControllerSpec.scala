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

class EduBoolControllerSpec extends ControllerSpecBase {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val controller = inject[EduBoolController]

  "EduBoolController" when {
    "calling show()" must {
      "return 200 (Ok) if user exists and previous fields done" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
      }
      "return HTML if user exists and previous fields done" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool").withSession("user" -> Json.toJson(user.copy()).toString))
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      "return 200 (Ok) if user exists and field already filled" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), Some(true), None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
        contentAsString(result) must include ("checked")
      }
      "return 303 if user doesn't exist" in {
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool").withSession())
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to dob if dob field missing" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/edu-bool")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/dob")
      }
    }

    "calling post()" must {
      "return 303 (Redirect) to edu-date if user exists and form filled correctly with false" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("education" -> "false").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/edu-date")
      }
      "return 303 (Redirect) to employment if user exists and form filled correctly with true" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("education" -> "true").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/employment")
      }
      "return 303 and redirect to home if user doesn't exist" in {
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post"))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 400 (BAD_REQUEST) and refresh if form has error" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("education" -> "").withCSRFToken
        )
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include ("Choose an education option")
      }
      "return 303 and redirect to summary if summary true" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), None, None, Some("Unemployed"), None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user.copy()).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("education" -> "true").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/summary")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("education" -> "true").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to dob if dob field missing" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(POST, "/another-tax-service/edu-bool-post")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("education" -> "true").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/dob")
      }
    }

    "calling back()" must {
      "return 303 (Redirect)" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("first"), Some(date), None, None, None, None)
        val result = controller.back()(FakeRequest(POST, "/another-tax-service/edu-bool-back")
          .withSession("user" -> Json.toJson(user.copy()).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/dob")
      }
    }

  }

}
