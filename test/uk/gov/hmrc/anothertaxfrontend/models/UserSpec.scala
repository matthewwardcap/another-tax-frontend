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

package uk.gov.hmrc.anothertaxfrontend.models

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserSpec extends UnitSpec{

  private val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
  private val date = LocalDate.parse("19-03-2000", format)
  private val user = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), Some(date), Some("Full-time Employment"), Some(30000))
  private val userJson: JsValue = Json.obj(
    "firstName" -> user.firstName,
    "middleName" -> user.middleName.getOrElse(fail("middle name not set ona complete name")),
    "lastName" -> user.lastName,
    "dob" -> user.dob,
    "education" -> user.education,
    "educationDate" -> user.educationDate,
    "employmentStatus" -> user.employmentStatus,
    "salary" -> user.salary
  )

  "User" when {

    "serialising" must {
      "render the user" in {
        Json.toJson(user) mustBe userJson
      }
    }

    "deserializing" must {
      "generate a complete name when the middle name is present" in {
        Json.fromJson[User](userJson).getOrElse(fail("could not parse json")) mustBe user
      }
    }

  }
}