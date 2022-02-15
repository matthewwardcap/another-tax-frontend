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

import org.jsoup.nodes.{Document, Element}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper.CSRFRequest
import play.api.test.{FakeRequest, Injecting}
import uk.gov.hmrc.anothertaxfrontend.core.UnitSpec
import uk.gov.hmrc.http.SessionKeys

import scala.collection.JavaConverters._

trait ViewSpecBase extends UnitSpec with GuiceOneAppPerSuite with Injecting {

  implicit lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest("GET", "/")
      .withSession(SessionKeys.sessionId -> "foo")
      .withCSRFToken
      .asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  lazy val messagesControllerComponents: MessagesControllerComponents = inject[MessagesControllerComponents]

  implicit lazy val messagesApi: MessagesApi = inject[MessagesApi]
  implicit val messages: Messages = messagesApi.preferred(fakeRequest)

  def elementText(selector: String)(implicit document: Document): String = {
    element(selector).text()
  }

  def elementAttributes(cssSelector: String)(implicit document: Document): Map[String, String] = {
    val attributes = element(cssSelector).attributes.asList().asScala.toList
    attributes.map(attribute => (attribute.getKey, attribute.getValue)).toMap
  }

  def element(cssSelector: String)(implicit document: Document): Element = {
    val elements = document.select(cssSelector)

    if (elements.size == 0) {
      fail(s"No element exists with the selector '$cssSelector'")
    }

    elements.first()
  }

}
