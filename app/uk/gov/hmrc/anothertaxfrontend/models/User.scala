package uk.gov.hmrc.anothertaxfrontend.models

import java.util.Date

case class User(
                 firstName:String,
                 middleName:String,
                 lastName:String,
                 dob:Date,
                 education:Boolean,
                 educationDate:Date,
                 employmentStatus:String,
                 salary:BigDecimal
               )


object User {
  val firstName         = "firstName"
  val middleName        = "middleName"
  val lastName          = "lastName"
  val dob               = "dob"
  val education         = "education"
  val educationDate     = "educationDate"
  val employmentStatus  = "employmentStatus"
  val salary            = "salary"
}
