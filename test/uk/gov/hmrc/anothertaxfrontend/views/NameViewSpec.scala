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
import uk.gov.hmrc.anothertaxfrontend.forms.{Data, NameForm}
import uk.gov.hmrc.anothertaxfrontend.views.html.NamePage

class NameViewSpec extends ViewSpecBase {

  "NamePage" when {
    "rendering a view with no errors" must {

      lazy val target: NamePage = inject[NamePage]
      lazy val form = NameForm.form
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Enter your name"
      }

      "render a form with a POST method" in {
        elementAttributes("form") must contain ("method" -> "POST")
      }

      "render a form that submits data to the correct route" in {
        elementAttributes("form") must contain ("action" -> uk.gov.hmrc.anothertaxfrontend.controllers.routes.NameController.post.url)
      }

      "have a text input for the first name" in {
        elementAttributes("#firstName") must contain ("type" -> "text")
      }

      "have a label associated to the first name input with the correct text" in {
        elementText("""label[for="firstName"]""") mustBe "First Name"
      }

      "have a text input for the middle names" in {
        elementAttributes("#middleName") must contain ("type" -> "text")
      }

      "have a label associated to the middle name input with the correct text" in {
        elementText("""label[for="middleName"]""") mustBe "Middle Name"
      }

      "have a text input for the last name" in {
        elementAttributes("#lastName") must contain ("type" -> "text")
      }

      "have a label associated to the last name input with the correct text" in {
        elementText("""label[for="lastName"]""") mustBe "Last Name"
      }

      "have a button to submit form data" in {
        elementText("button") mustBe "Save and continue"
      }
    }

    "rendering a view with errors" must {

      lazy val target: NamePage = inject[NamePage]
      lazy val form = NameForm.form.bindFromRequest()
      lazy val result: Html = target(form)
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have an error message against the first name in the form" in {
        elementText("form p#firstName-error") mustBe "Error: This field is required"
      }

      "have an error message against the last name in the form" in {
        elementText("form p#lastName-error") mustBe "Error: This field is required"
      }

    }
  }
}
