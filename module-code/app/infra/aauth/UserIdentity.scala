package infra.aauth

import scala.concurrent.Await
import infra.mongo.MongoDomain
import securesocial.core._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * @author alari
 * @since 7/3/13 10:08 PM
 */
case class UserIdentity(
                         _id: MongoDomain.Oid.Id,

                         profileId: Option[String],

                         identityId: IdentityId,

                         firstName: String,
                         lastName: String,
                         fullName: String,

                         email: Option[String],
                         avatarUrl: Option[String],

                         authMethod: AuthenticationMethod,
                         oAuth1Info: Option[OAuth1Info],
                         oAuth2Info: Option[OAuth2Info],

                         passwordInfo: Option[PasswordInfo]
                         ) extends Identity with MongoDomain.Oid with Serializable {
  def toLowerCased = copy(identityId = identityId.copy(userId = identityId.userId.toLowerCase), email = email.map(_.toLowerCase))
}

object UserIdentity {
  /**
   * Finds by user id or creates a new user instance
   * @param user
   * @return
   */
  implicit def identity2user(user: Identity): UserIdentity = {
    Await.result(UserIdentityDAO.findByIdentityId(user.identityId), AuthPlugins.Timeout).getOrElse(
      UserIdentity(None, None, user.identityId,
        user.firstName, user.lastName, user.fullName,
        user.email, user.avatarUrl,
        user.authMethod,
        user.oAuth1Info, user.oAuth2Info, user.passwordInfo)
    )
  }
}

