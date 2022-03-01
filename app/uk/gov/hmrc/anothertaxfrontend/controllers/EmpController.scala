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
import uk.gov.hmrc.anothertaxfrontend.forms.{EmpData, EmpForm}
import uk.gov.hmrc.anothertaxfrontend.forms.EmpForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.EmpPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EmpController @Inject()(
                                   mcc: MessagesControllerComponents,
                                   empPage: EmpPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val homeRoute = routes.HelloWorldController.helloWorld
    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) =>
          if (user.education.isDefined) {
            if (!user.education.getOrElse(false) && user.educationDate.isEmpty) {
              Future.successful(Redirect(routes.EduDateController.show))
            } else {
              val filledForm = user.employmentStatus match {
                case None => EmpForm.form
                case Some(status) => EmpForm.form.fill(EmpData(status))
              }
              Future.successful(Ok(empPage(filledForm, summary)))
            }
          } else Future.successful(Redirect(routes.EduBoolController.show))
      }
    }
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val controllerRoute = if (!summary) routes.SalaryController.show else routes.SummaryController.show
    val homeRoute = routes.HelloWorldController.helloWorld

    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) =>
          form.bindFromRequest().fold(
            formWithErrors => Future.successful(BadRequest(empPage(formWithErrors, summary))),
            dataForm => {
              if (dataForm.employmentStatus == "Unemployed") {
                val updatedUser = user.copy(employmentStatus = Option(dataForm.employmentStatus), salary = None)
                val updatedUserAsJson = Json.toJson(updatedUser).toString()
                Future.successful(Redirect(routes.SummaryController.show).addingToSession("user" -> updatedUserAsJson))
              } else {
                val updatedUser = user.copy(employmentStatus = Option(dataForm.employmentStatus))
                val updatedUserAsJson = Json.toJson(updatedUser).toString()
                Future.successful(Redirect(routes.SalaryController.show).addingToSession("user" -> updatedUserAsJson))
              }
            }
          )
      }
    }
  }

  def back: Action[AnyContent] = Action.async { implicit request =>
    val homeRoute = routes.HelloWorldController.helloWorld
    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) => user.educationDate match {
          case None => Future.successful(Redirect(routes.EduBoolController.show))
          case Some(value) => Future.successful(Redirect(routes.EduDateController.show))
        }
      }
    }
  }
}
