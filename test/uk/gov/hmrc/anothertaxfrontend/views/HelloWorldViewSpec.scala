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
import play.twirl.api.Html
import uk.gov.hmrc.anothertaxfrontend.views.html.HelloWorldPage

class HelloWorldViewSpec extends ViewSpecBase {

  "Hello World View" when {
    "rendering a view" should {
      val target: HelloWorldPage = inject[HelloWorldPage]
      val result: Html = target()
      lazy implicit val document: Document = Jsoup.parse(result.body)

      "have the correct heading" in {
        elementText("h1") mustBe "Another Tax Service"
      }

      "have a button to move pages" in {
        elementText("a.govuk-button") mustBe "Start now"
      }
    }
  }
}
