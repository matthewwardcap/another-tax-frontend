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

class SummaryControllerSpec extends ControllerSpecBase {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val controller = inject[SummaryController]

  "SalaryController" when {
    "calling show()" must {
      "return 200 (Ok) if user exists and previous fields done and employmentStatus Full-time Employment" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Full-time Employment"), Some(30000))
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
        contentAsString(result) must include ("first middle last")
        contentAsString(result) must include ("19-03-2000")
        contentAsString(result) must include ("Yes")
        contentAsString(result) must include ("Full-time Employment")
        contentAsString(result) must include ("£30,000.00")
      }
      "return HTML if user exists and previous fields done and employmentStatus Full-time Employment" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Full-time Employment"), Some(30000))
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
      }
      "return 200 (Ok) if user exists and previous fields done and employmentStatus Part-time Employment" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Part-time Employment"), Some(30000))
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
        contentAsString(result) must include ("first middle last")
        contentAsString(result) must include ("19-03-2000")
        contentAsString(result) must include ("Yes")
        contentAsString(result) must include ("Part-time Employment")
        contentAsString(result) must include ("£30,000.00")
      }
      "return 200 (Ok) if user exists and previous fields done and employmentStatus Unemployed Employment and salary missing" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Unemployed"), None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe OK
        contentAsString(result) must include ("first middle last")
        contentAsString(result) must include ("19-03-2000")
        contentAsString(result) must include ("Yes")
        contentAsString(result) must include ("Unemployed")
      }
      "return 303 if user exists and previous fields done and employmentStatus Full-time Employment and Salary missing" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Full-time Employment"), None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/salary")
      }
      "return 303 if user exists and previous fields done and employmentStatus Part-time Employment and Salary missing" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Part-time Employment"), None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/salary")
      }
      "return 303 if user exists and previous fields done and employmentStatus missing" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, None, None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/employment")
      }
      "return 303 if user exists and previous fields done and eduDate missing and education false" in {
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("19-03-2000", format)
        val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some("Unemployed"), None)
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession("user" -> Json.toJson(user.copy()).toString))
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service/edu-date")
      }
      "return 303 if user doesn't exist" in {
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary").withSession())
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
      "return 303 and redirect to home if user incorrect" in {
        val user = "test"
        val result = controller.show()(FakeRequest(GET, "/another-tax-service/summary")
          .withSession("user" -> Json.toJson(user).toString, "summary" -> Json.toJson(true).toString)
        )
        status(result) mustBe 303
        header(LOCATION, result) mustBe Some("/another-tax-service")
      }
    }

  }

}
