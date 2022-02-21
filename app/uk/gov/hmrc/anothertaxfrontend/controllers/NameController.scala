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

import uk.gov.hmrc.anothertaxfrontend.forms.NameForm
import uk.gov.hmrc.anothertaxfrontend.forms.NameForm._
import uk.gov.hmrc.anothertaxfrontend.models.User._
import uk.gov.hmrc.anothertaxfrontend.models.User
import uk.gov.hmrc.anothertaxfrontend.views.html.NamePage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.api.libs.json._
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class NameController @Inject()(
                                      mcc: MessagesControllerComponents,
                                      namePage: NamePage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(namePage(NameForm.form)))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val user = request.session.get("user").map(user => Json.parse(user).as[User])
    /*
    request.session.get("user").map(user => Json.parse(user).as[User]) match {
      case None => uk.gov.hmrc.anothertaxfrontend.controllers.routes.HelloWorldController.helloWorld
      case Some(user) => rest of code here
    }
    */
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val controllerRoute = if (!summary) uk.gov.hmrc.anothertaxfrontend.controllers.routes.DobController.show else
      uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryController.show
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(namePage(formWithErrors))),
        dataForm => Future.successful(Redirect(controllerRoute)
          .addingToSession(
            "user" -> Json.toJson(user.map(us => us.copy(
              Option(dataForm.firstName),
              dataForm.middleName,
              Option(dataForm.lastName)
            ))).toString
          )
        )
      )
  }
}
