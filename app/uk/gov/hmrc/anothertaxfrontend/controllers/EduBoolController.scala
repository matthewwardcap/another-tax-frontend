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

import play.api.libs.json.Json
import uk.gov.hmrc.anothertaxfrontend.forms.EduBoolForm
import uk.gov.hmrc.anothertaxfrontend.forms.EduBoolForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.EduBoolPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EduBoolController @Inject()(
                               mcc: MessagesControllerComponents,
                               eduPage: EduBoolPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    Future.successful(Ok(eduPage(EduBoolForm.form, summary)))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val user = request.session.get("user").map(user => Json.parse(user).as[User])
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val controllerRoute = if (!summary) uk.gov.hmrc.anothertaxfrontend.controllers.routes.EduDateController.show else
      uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryController.show
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(eduPage(formWithErrors, summary))),
        dataForm => Future.successful(Redirect(controllerRoute)
          .addingToSession(
            "user" -> Json.toJson(user.map(us => us.copy(
              education = Option(dataForm.education)
            ))).toString
          )
        )
      )
  }

  def back: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.DobController.show))
  }
}
