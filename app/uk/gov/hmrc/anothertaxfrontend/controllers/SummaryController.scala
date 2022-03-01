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

import uk.gov.hmrc.anothertaxfrontend.models.User._
import uk.gov.hmrc.anothertaxfrontend.models.User
import uk.gov.hmrc.anothertaxfrontend.views.html.SummaryPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import play.api.libs.json._
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class SummaryController @Inject()(
                                mcc: MessagesControllerComponents,
                                summaryPage: SummaryPage)
  extends FrontendController(mcc) {

  def show: Action[AnyContent] = Action.async { implicit request =>
    val homeRoute = routes.HelloWorldController.helloWorld

    request.session.get("user") match {
      case None => Future.successful(Redirect(homeRoute))
      case Some(userString) => Json.parse(userString).asOpt[User] match {
        case None => Future.successful(Redirect(homeRoute))
        case Some(user) => if (user.firstName.isEmpty || user.lastName.isEmpty || user.dob.isEmpty || user.education.isEmpty || user.employmentStatus.isEmpty) {
          Future.successful(Redirect(routes.EmpController.show))
        } else if (!user.education.get && user.educationDate.isEmpty) {
          Future.successful(Redirect(routes.EduDateController.show))
        } else if (user.employmentStatus.get != "Unemployed" && user.salary.isEmpty) {
          Future.successful(Redirect(routes.SalaryController.show))
        } else {
          val summary = true
          Future.successful(Ok(summaryPage(user)).addingToSession("summary" -> Json.toJson(summary).toString))
        }
      }
    }
  }

}
