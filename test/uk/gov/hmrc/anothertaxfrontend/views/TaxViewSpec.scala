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

package uk.gov.hmrc.anothertaxfrontend.views

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.data.FormBinding.Implicits.formBinding
import play.twirl.api.Html
import uk.gov.hmrc.anothertaxfrontend.models.User
import uk.gov.hmrc.anothertaxfrontend.views.html.TaxPage

import scala.math.BigDecimal.{RoundingMode, double2bigDecimal}

class TaxViewSpec extends ViewSpecBase {

  "TaxPage" when {
    "rendering a view with no decimal" must {

      lazy val target: TaxPage = inject[TaxPage]
      lazy val taxAmount: BigDecimal = 3000.setScale(2, RoundingMode.HALF_UP)
      lazy val result: Html = target(taxAmount)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Application Complete"
      }

      "have a inset text associated to the Tax output with the correct text" in {
        elementText("""[class="govuk-inset-text"]""") mustBe "Total amount payable: £3,000.00"
      }

    }

    "rendering a view with decimal" must {

      lazy val target: TaxPage = inject[TaxPage]
      lazy val taxAmount: BigDecimal = 3000.056.setScale(2, RoundingMode.HALF_UP)
      lazy val result: Html = target(taxAmount)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Application Complete"
      }

      "have a inset text associated to the Tax output with the correct text" in {
        elementText("""[class="govuk-inset-text"]""") mustBe "Total amount payable: £3,000.06"
      }

    }

  }
}
