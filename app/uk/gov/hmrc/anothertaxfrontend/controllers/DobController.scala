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

import uk.gov.hmrc.anothertaxfrontend.forms.{DobData, DobForm}
import uk.gov.hmrc.anothertaxfrontend.forms.DobForm._
import uk.gov.hmrc.anothertaxfrontend.views.html.DobPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.anothertaxfrontend.models.User
import play.api.libs.json._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DobController @Inject()(
                               mcc: MessagesControllerComponents,
                               dobPage: DobPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val homeRoute = routes.HelloWorldController.helloWorld
    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) => if (user.firstName.isDefined) {
          val filledForm = user.dob match {
            case None => DobForm.form
            case Some(date) => DobForm.form.fill(DobData(date.getDayOfMonth, date.getMonthValue, date.getYear))
          }
          Future.successful(Ok(dobPage(filledForm, summary)))
        } else Future.successful(Redirect(routes.NameController.show))
      }
    }
  }

  def post: Action[AnyContent] = Action.async { implicit request =>
    val summary = request.session.get("summary").exists(summary => Json.parse(summary).as[Boolean])
    val controllerRoute = if (!summary) uk.gov.hmrc.anothertaxfrontend.controllers.routes.EduBoolController.show else
      uk.gov.hmrc.anothertaxfrontend.controllers.routes.SummaryController.show
    val homeRoute = uk.gov.hmrc.anothertaxfrontend.controllers.routes.HelloWorldController.helloWorld
    val format = DateTimeFormatter.ofPattern("d-M-yyyy")

    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) =>
          form.bindFromRequest().fold(
            formWithErrors => Future.successful(BadRequest(dobPage(formWithErrors, summary))),
            dataForm => {
              val month = dataForm.month.toString.format(DateTimeFormatter.ofPattern("M"))
              val date = Some(LocalDate.parse(dataForm.day.toString+"-"+month+"-"+dataForm.year.toString, format))
              val updatedUser = user.copy(dob = date)
              val updatedUserAsJson = Json.toJson(updatedUser).toString()

              Future.successful(Redirect(controllerRoute).addingToSession("user" -> updatedUserAsJson)
              )
            }
          )
      }
    }
  }

  def back: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Redirect(uk.gov.hmrc.anothertaxfrontend.controllers.routes.NameController.show))
  }
}
