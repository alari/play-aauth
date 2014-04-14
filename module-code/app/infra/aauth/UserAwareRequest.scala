package infra.aauth

import play.api.mvc.{WrappedRequest, Request}

/**
 * @author alari
 * @since 2/20/14
 */
case class UserAwareRequest[A](profileIdOpt: Option[ProfileId], request: Request[A]) extends WrappedRequest(request) with UserAware
