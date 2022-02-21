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
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    Future.successful(Ok(salaryPage(SalaryForm.form, summary)))
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val homeRoute = uk.gov.hmrc.anothertaxfrontend.controllers.routes.HelloWorldController.helloWorld
    val controllerRoute = uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryController.show

    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) =>
          form.bindFromRequest().fold(
            formWithErrors => Future.successful(BadRequest(salaryPage(formWithErrors, summary))),
            dataForm => {
              val updatedUser = user.copy(salary = Option(dataForm.salary))
              val updatedUserAsJson = Json.toJson(updatedUser).toString()

              Future.successful(Redirect(controllerRoute).addingToSession("user" -> updatedUserAsJson)
              )
            }
          )
      }
    }
  }

  def back: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.EmpController.show))
  }
}
