package infra.aauth

import play.api.mvc._
import scala.concurrent.Future
import play.api.mvc.SimpleResult
import infra.wished.Unwished


object UserRequiredAction extends ActionBuilder[UserRequiredRequest] {
   implicit def ctx = executionContext

   protected def invokeBlock[A](request: Request[A], block: (UserRequiredRequest[A]) => Future[SimpleResult]): Future[SimpleResult] =
     Unwished.wrap {
       AuthPlugins.requestAuthenticator.getFromRequest(request) match {
         case (Some(p), f) =>
           block(UserRequiredRequest(p, request)).map(f)
         case (None, f) =>
           Future successful f(AuthPlugins.unauthorizedResponse(request))
       }
     }(request)
 }