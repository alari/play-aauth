package mirari.aauth

import scala.concurrent.{ExecutionContext, Future}
import mirari.wished.Unwished

/**
  * @author alari
  * @since 2/20/14
  */
case class ProfileId(id: String, userIdentity: Option[UserIdentity] = None) {
   def identity(implicit ec: ExecutionContext): Future[UserIdentity] = userIdentity.map(Future.successful).getOrElse(UserIdentityDAO.getById(id))

   override def toString = "profileId=" + id
 }

object ProfileId {
   implicit def fromIdentity(user: UserIdentity): ProfileId = ProfileId(user.profileId.getOrElse {
     play.api.Logger.error("Cannot convert user identity to profile id: " + user)
     throw Unwished.InternalServerError("Cannot get profile id")
   }, Some(user))

   implicit def fromString(id: String): ProfileId = ProfileId(id, None)

   implicit def toStringId(pid: ProfileId): String = pid.id
 }