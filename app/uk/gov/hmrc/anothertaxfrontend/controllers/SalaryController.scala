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

import uk.gov.hmrc.anothertaxfrontend.forms.SalaryForm
import uk.gov.hmrc.anothertaxfrontend.forms.SalaryForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.SalaryPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User
import play.api.libs.json._
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SalaryController @Inject()(
                               mcc: MessagesControllerComponents,
                               salaryPage: SalaryPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(salaryPage(SalaryForm.form)))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val user = request.session.get("user").map(user => Json.parse(user).as[User])
    form
      .bindFromRequest()
      .fold(
        formWithErrors => Future.successful(BadRequest(salaryPage(formWithErrors))),
        dataForm => Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.HelloWorldController.helloWorld)
          .addingToSession(
            "user" -> Json.toJson(user.map(us => us.copy(
              salary = Option(dataForm.salary)
            ))).toString
          )
        )
      )
  }
}
