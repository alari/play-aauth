package infra.aauth

import play.api.mvc.{SimpleResult, RequestHeader}
import play.api.{Mode, Play, Plugin}
import Play.current
import securesocial.core.{Authenticator, UserService, SecureSocial}

/**
 * @author alari
 * @since 2/20/14
 */
trait RequestAuthenticator extends Plugin {
  def getFromRequest(rh: RequestHeader): (Option[ProfileId], SimpleResult => SimpleResult)
}

private[aauth] object RequestAuthenticator extends RequestAuthenticator {
  override def getFromRequest(rh: RequestHeader) =
    if (Play.mode == Mode.Test) {
      (rh.getQueryString("profileId").map(ProfileId(_)), {
        r => r
      })
    } else {
      (for {
        authenticator <- SecureSocial.authenticatorFromRequest(rh)
        user <- UserService.find(authenticator.identityId)
      } yield {
        Authenticator.save(authenticator.touch)
        ProfileId.fromIdentity(user) -> {
          r: SimpleResult => r.withCookies(authenticator.toCookie)
        }
      }).map(pr => (Some(pr._1), pr._2)).getOrElse((None, {
        r: SimpleResult => r.discardingCookies(Authenticator.discardingCookie)
      }))
    }
}