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

import uk.gov.hmrc.anothertaxfrontend.forms.{EduDateData, EduDateForm}
import uk.gov.hmrc.anothertaxfrontend.forms.EduDateForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.EduDatePage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User
import play.api.libs.json._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class EduDateController @Inject()(
                               mcc: MessagesControllerComponents,
                               eduDatePage: EduDatePage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val homeRoute = routes.HelloWorldController.helloWorld
    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) => if (user.education.isDefined) {
          if (!user.education.get) {
            val filledForm = user.educationDate match {
              case None => EduDateForm.form
              case Some(date) => EduDateForm.form.fill(EduDateData(date.getDayOfMonth, date.getMonthValue, date.getYear))
            }
            Future.successful(Ok(eduDatePage(filledForm, summary)))
          } else Future.successful(Redirect(routes.SummaryController.show))
        } else Future.successful(Redirect(routes.EduBoolController.show))
      }
    }
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val controllerRoute = if (!summary) routes.EmpController.show else routes.SummaryController.show
    val homeRoute = routes.HelloWorldController.helloWorld
    val format = DateTimeFormatter.ofPattern("d-M-yyyy")

    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) =>
          form.bindFromRequest().fold(
            formWithErrors => Future.successful(BadRequest(eduDatePage(formWithErrors, summary))),
            dataForm => {
              val month = dataForm.month.toString.format(DateTimeFormatter.ofPattern("M"))
              val date = Some(LocalDate.parse(dataForm.day.toString+"-"+month+"-"+dataForm.year.toString, format))
              val updatedUser = user.copy(educationDate = date)
              val updatedUserAsJson = Json.toJson(updatedUser).toString()

              Future.successful(Redirect(controllerRoute).addingToSession("user" -> updatedUserAsJson)
              )
            }
          )
      }
    }
  }

  def back: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(routes.EduBoolController.show))
  }
}
