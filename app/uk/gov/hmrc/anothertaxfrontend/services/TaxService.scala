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

import uk.gov.hmrc.anothertaxfrontend.models.User

import java.time.{LocalDate, Period}
import javax.inject.{Inject, Singleton}

@Singleton
class TaxService {

  def post(user: User): Either[String, BigDecimal] = {

    lazy val result = (i: Double) => user.salary match {
      case None => Left("Salary doesn't exist")
      case Some(sal) => Right(sal - (sal * i))
    }

    if (user.education.getOrElse(true) || user.employmentStatus.getOrElse("") == "Unemployed" || Period.between(user.dob.getOrElse(LocalDate.now), LocalDate.now).getYears < 18) {
      Right(BigDecimal(0))
    } else if (user.employmentStatus.getOrElse("") == "Part-time Employment") {
      val tax = 0.95
      result(tax)
    } else if (user.employmentStatus.getOrElse("") == "Full-time Employment") {
      val tax = 0.90
      result(tax)
    } else {
      Left("Error")
    }
  }

}
