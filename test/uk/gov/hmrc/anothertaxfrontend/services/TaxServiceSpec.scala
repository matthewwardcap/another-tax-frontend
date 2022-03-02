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

package uk.gov.hmrc.anothertaxfrontend.services

import org.scalamock.handlers.CallHandler
import uk.gov.hmrc.anothertaxfrontend.controllers.ControllerSpecBase
import uk.gov.hmrc.anothertaxfrontend.models.User

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class TaxServiceSpec extends ControllerSpecBase{

  trait Test {
    lazy val service = new TaxService()

    val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val date: LocalDate = LocalDate.parse("19-03-2000", format)
    val fullUser: User        = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some("Full-time Employment"), Some(30000))
    val partUser: User        = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some("Part-time Employment"), Some(30000))
    val unemployedUser: User  = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some("Unemployed"), None)
    val trueUser: User        = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true), None, Some("Full-time Employment"), Some(30000))
    val missingSalUser: User  = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some("Full-time Employment"), None)
    val missingEmpUser: User  = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(false), None, Some(""), Some(30000))
    val missingEduUser: User  = User(Some("first"), Some("middle"), Some("last"), Some(date), None, None, Some("Full-time Employment"), Some(30000))
  }

  "TaxController" when {
    "calling post()" must {
      "return Right(val)" when {
        "User is full time and valid" in new Test {
          val result: Either[String, BigDecimal] = service.post(fullUser)
          result mustBe Right{3000}
        }
        "User is part time and valid" in new Test {
          val result: Either[String, BigDecimal] = service.post(partUser)
          result mustBe Right{1500}
        }
        "User is unemployed and valid" in new Test {
          val result: Either[String, BigDecimal] = service.post(unemployedUser)
          result mustBe Right{0}
        }
        "User is full time and valid and true" in new Test {
          val result: Either[String, BigDecimal] = service.post(trueUser)
          result mustBe Right{0}
        }
        "User is missing education" in new Test {
          val result: Either[String, BigDecimal] = service.post(missingEduUser)
          result mustBe Right{0}
        }
      }
      "return Left(string)" when {
        "User is missing salary" in new Test {
          val result: Either[String, BigDecimal] = service.post(missingSalUser)
          result mustBe Left{"Salary doesn't exist"}
        }
        "User is employmentStatus" in new Test {
          val result: Either[String, BigDecimal] = service.post(missingEmpUser)
          result mustBe Left{"Error"}
        }
      }
    }
  }

}
