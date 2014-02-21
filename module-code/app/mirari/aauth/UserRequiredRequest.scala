package mirari.aauth

import play.api.mvc.{WrappedRequest, Request}

/**
 * @author alari
 * @since 2/20/14
 */
case class UserRequiredRequest[A](profileId: ProfileId, request: Request[A]) extends WrappedRequest(request) with UserAware with UserRequired {
  override def profileIdOpt = Some(profileId)
}
