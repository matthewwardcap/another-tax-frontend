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

class NameControllerSpec extends ControllerSpecBase {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val controller = inject[NameController]

  "NameController" when {
    "calling show()" must {
      "return 200 (Ok) if user exists" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/name").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
      }
      "return HTML if user exists" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/name").withSession("user" -> Json.toJson(user.copy()).toString))
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      "return 303 if user doesn't exist" in {
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/name").withSession())
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/name")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("firstName" -> "first", "middleName" -> "", "lastName" -> "last").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
    }

    "calling post()" must {
      "return 303 (Redirect) if user exists" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("firstName" -> "first", "middleName" -> "", "lastName" -> "last").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/dob")
      }
      "return 303 and redirect to home if user doesn't exist" in {
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post"))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 400 (BAD_REQUEST) and refresh if form missing first name" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("firstName" -> "", "middleName" -> "", "lastName" -> "last").withCSRFToken
        )
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include ("Enter your first name")
      }
      "return 400 (BAD_REQUEST) and refresh if form missing last name" in {
        val user = User(None, None, None, None, None, None, None, None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post")
          .withSession("user" -> Json.toJson(user.copy()).toString)
          .withFormUrlEncodedBody("firstName" -> "first", "middleName" -> "", "lastName" -> "").withCSRFToken
        )
        status(result) mustBe BAD_REQUEST
        contentAsString(result) must include ("Enter your last name")
      }
      "return 303 and redirect to summary if summary true" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(None, None, None, Some(date), Some(false), None, Some("Unemployed"), None)
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post")
          .withSession("user" -> Json.toJson(user.copy()).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("firstName" -> "first", "middleName" -> "", "lastName" -> "last").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/summary")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.post()(FakeRequest(POST, "/another-tax-service/name-post")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
          .withFormUrlEncodedBody("firstName" -> "first", "middleName" -> "", "lastName" -> "last").withCSRFToken
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
    }

  }

}
