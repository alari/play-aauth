package mirari.aauth

import play.api.mvc.{SimpleResult, Request, ActionBuilder}
import scala.concurrent.Future
import mirari.wished.Unwished

/**
 * @author alari
 * @since 2/20/14
 */
object UserAwareAction extends ActionBuilder[UserAwareRequest] {
  implicit def ctx = executionContext

  protected def invokeBlock[A](request: Request[A], block: (UserAwareRequest[A]) => Future[SimpleResult]): Future[SimpleResult] =
    Unwished.wrap {
      val (pid, f) = AuthPlugins.requestAuthenticator.getFromRequest(request)
      block(UserAwareRequest(pid, request)).map(f)
    }
}
