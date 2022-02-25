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
import uk.gov.hmrc.anothertaxfrontend.views.html.SummaryPage

class SummaryViewSpec extends ViewSpecBase {

  "SummaryPage" when {
    "rendering a view with no errors" must {

      lazy val target: SummaryPage = inject[SummaryPage]
      lazy val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
      lazy val date = format.parse("19-03-2000")
      lazy val user: User = User(Some("first"), Some("middle"), Some("last"), Some(date), Some(true),
        Some(date), Some("Full-time Employment"), Some(30000))
      lazy val result: Html = target(user)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Check your answers"
      }

      "have a label associated to the Name output with the correct text" in {
        elementText("""[class="govuk-summary-list__value"]""") mustBe "first middle last"
      }

      "have a label associated to the Date of Birth output with the correct text" in {
        document.select("""[class="govuk-summary-list__value"]""").get(1).text mustBe "19-03-2000"
      }

      "have a label associated to the Education output with the correct text" in {
        document.select("""[class="govuk-summary-list__value"]""").get(2).text mustBe "Yes"
      }

      "have a label associated to the Education date output with the correct text" in {
        document.select("""[class="govuk-summary-list__value"]""").get(3).text mustBe "19-03-2000"
      }

      "have a label associated to the Employment Status output with the correct text" in {
        document.select("""[class="govuk-summary-list__value"]""").get(4).text mustBe "Full-time Employment"
      }

      "have a label associated to the Salary output with the correct text" in {
        document.select("""[class="govuk-summary-list__value"]""").get(5).text mustBe "£30000"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Save and continue"
      }

    }

    "rendering a view with missing optionals" must {

      lazy val target: SummaryPage = inject[SummaryPage]
      lazy val format = new java.text.SimpleDateFormat("dd-MM-yyyy")
      lazy val date = format.parse("19-03-2000")
      lazy val user: User = User(Some("first"), None, Some("last"), Some(date), Some(false),
        None, Some("Unemployed"), None)
      lazy val result: Html = target(user)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "have a label associated to the Name output with the correct text" in {
        elementText("""[class="govuk-summary-list__value"]""") mustBe "first last"
      }

      "have a label associated to the Education finish date empty" in {
        document.select("""[class="govuk-summary-list__value"]""").get(3).text mustBe ""
      }

      "have a label associated to the Salary empty" in {
        document.select("""[class="govuk-summary-list__value"]""").get(5).text mustBe ""
      }

    }

  }
}
