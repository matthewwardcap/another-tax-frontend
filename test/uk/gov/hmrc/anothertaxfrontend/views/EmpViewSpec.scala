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
import uk.gov.hmrc.anothertaxfrontend.forms.EmpForm
import uk.gov.hmrc.anothertaxfrontend.views.html.EmpPage

class EmpViewSpec extends ViewSpecBase {

  "EmpPage" when {
    "rendering a view with no errors and not reached summary" must {

      lazy val target: EmpPage = inject[EmpPage]
      lazy val form = EmpForm.form
      lazy val result: Html = target(form, summary = false)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "What is your employment status?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EmpController.post.url)
      }

      "have a radio input for Unemployed" in {
        elementAttributes("#employmentStatus") must contain ("type" -> "radio")
      }

      "have a label associated to the Unemployed radio with the correct text" in {
        elementText("""label[for="employmentStatus"]""") mustBe "Unemployed"
      }

      "have a radio input for Full-time Employment" in {
        elementAttributes("#employmentStatus-2") must contain ("type" -> "radio")
      }

      "have a label associated to the Full-time Employment radio with the correct text" in {
        elementText("""label[for="employmentStatus-2"]""") mustBe "Full-time Employment"
      }

      "have a radio input for Part-time Employment" in {
        elementAttributes("#employmentStatus-3") must contain ("type" -> "radio")
      }

      "have a label associated to the Part-time Employment radio with the correct text" in {
        elementText("""label[for="employmentStatus-3"]""") mustBe "Part-time Employment"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Save and continue"
      }

      "have a button to go back to previous page" in {
        elementText("a.govuk-back-link") mustBe "Back"
      }

    }

    "rendering a view with no errors and reached summary" must {

      lazy val target: EmpPage = inject[EmpPage]
      lazy val form = EmpForm.form
      lazy val result: Html = target(form, summary = true)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "not have a button to go back to previous page" in {
        document.select("a.govuk-back-link") mustBe empty
      }

    }

    "rendering a view with no input errors" must {

      lazy val target: EmpPage = inject[EmpPage]
      lazy val form = EmpForm.form.bindFromRequest()
      lazy val result: Html = target(form, summary = false)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error message against the form" in {
        elementText("form p#employmentStatus-error") mustBe "Error: This field is required"
      }

    }

  }
}
