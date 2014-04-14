package infra.aauth

import play.api.Play.current
import scala.concurrent.duration.Duration
import play.api.mvc.{RequestHeader, Results, SimpleResult}
import play.api.libs.json.Json

/**
 * @author alari
 * @since 2/20/14
 */
object AuthPlugins {
  def requestAuthenticator: RequestAuthenticator = current.plugin[RequestAuthenticator].getOrElse(RequestAuthenticator)

  def profileService: ProfileService = current.plugin[ProfileService].getOrElse(ProfileService)

  def userIdentityDAO: UserIdentityDAO = current.plugin[UserIdentityDAO].getOrElse(UserIdentityDAO)

  def tokenService: TokenService = current.plugin[TokenService].getOrElse(TokenService)

  def unauthorizedResponse(rh: RequestHeader): SimpleResult = Results.Unauthorized(Json.obj("error" -> "Unauthorized"))

  implicit val Timeout = Duration.create(1, "second")
}
