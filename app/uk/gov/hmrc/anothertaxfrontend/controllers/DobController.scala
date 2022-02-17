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

package uk.gov.hmrc.anothertaxfrontend.controllers

import uk.gov.hmrc.anothertaxfrontend.forms.UserForm
import uk.gov.hmrc.anothertaxfrontend.forms.UserForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.DobPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DobController @Inject()(
                                      mcc: MessagesControllerComponents,
                                      dobPage: DobPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    //val x = form.data.getOrElse("first-name", "wrong")
    val filledForm = form.bindFromRequest()
    Future.successful(Ok(dobPage(filledForm)))
  }
}
