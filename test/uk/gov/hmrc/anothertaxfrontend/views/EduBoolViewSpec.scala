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
import uk.gov.hmrc.anothertaxfrontend.forms.EduBoolForm
import uk.gov.hmrc.anothertaxfrontend.views.html.EduBoolPage

class EduBoolViewSpec extends ViewSpecBase {

  "EduBoolPage" when {
    "rendering a view with no errors and not reached summary" must {

      lazy val target: EduBoolPage = inject[EduBoolPage]
      lazy val form = EduBoolForm.form
      lazy val result: Html = target(form, summary = false)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Are you still in education?"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.EduBoolController.post.url)
      }

      "have a radio input for No" in {
        elementAttributes("#education") must contain ("type" -> "radio")
      }

      "have a label associated to the No radio with the correct text" in {
        elementText("""label[for="education"]""") mustBe "No"
      }

      "have a radio input for Yes" in {
        elementAttributes("#education-2") must contain ("type" -> "radio")
      }

      "have a label associated to the Yes radio with the correct text" in {
        elementText("""label[for="education-2"]""") mustBe "Yes"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Save and continue"
      }

      "have a button to go back to previous page" in {
        elementText("a.govuk-back-link") mustBe "Back"
      }

    }

    "rendering a view with no errors and reached summary" must {

      lazy val target: EduBoolPage = inject[EduBoolPage]
      lazy val form = EduBoolForm.form
      lazy val result: Html = target(form, summary = true)
      lazy implicit val document: Document = Jsoup.parse(result.body)


      "not have a button to go back to previous page" in {
        document.select("a.govuk-back-link") mustBe empty
      }

    }

  }
}
