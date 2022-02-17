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
import uk.gov.hmrc.anothertaxfrontend.controllers.DobController
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
    val user = User(None, None, None, None, None, None, None, None)
    //val user1: User = Json.parse(request.session.get("user"))
    //val user: User = Json.toJson(request.session.get("user")).as[User]
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(namePage(formWithErrors))),
        dataForm => Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.DobController.show)
          .addingToSession(
            "user" -> Json.toJson(user.copy(
              Option(dataForm.firstName),
              dataForm.middleName,
              Option(dataForm.lastName))
            ).toString()
          )
        )
      )
  }
}
