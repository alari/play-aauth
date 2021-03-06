package infra.aauth

import infra.mongo.MongoDAO
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import securesocial.core._
import play.api.Plugin

/**
 * @author alari
 * @since 2/20/14
 */
private[aauth] object UserIdentityDAO extends MongoDAO.Oid[UserIdentity]("user.identity") with UserIdentityDAO with Plugin {
  implicit val authenticationMethodFormat = Json.format[AuthenticationMethod]
  implicit val oAuth1Format = Json.format[OAuth1Info]
  implicit val oAuth2Format = Json.format[OAuth2Info]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val identityIdFormat = Json.format[IdentityId]

  implicit val format = Json.format[UserIdentity]

  ensureIndex(unique = true, dropDups = true)("identityId" -> Descending)
  ensureIndex(unique = true, dropDups = true)("email" -> Descending, "identityId.providerId" -> Descending)

  /**
   * Find user by securesocial userid -- combined from id and provider
   *
   * @param id
   * @return
   */
  override def findByIdentityId(id: IdentityId)(implicit ec: ExecutionContext): Future[Option[UserIdentity]] = findOne(Json.obj("identityId" -> id))

  /**
   * Find user by email and securesocial provider id
   * @param email
   * @param providerId
   * @return
   */
  override def findByEmailAndProviderId(email: String, providerId: String)(implicit ec: ExecutionContext): Future[Option[UserIdentity]] = findOne(Json.obj("email" -> email.toLowerCase, "identityId.providerId" -> providerId))

  override def upsert(user: UserIdentity)(implicit ec: ExecutionContext): Future[UserIdentity] = {
    user._id match {
      case Some(_) =>
        update(user.toLowerCased)
      case _ =>
        findByIdentityId(user.identityId).flatMap {
          case Some(u) =>
            update(user.toLowerCased.copy(_id = u._id))
          case None =>
            AuthPlugins.profileService.create(user.toLowerCased.copy(_id = generateSomeId)).flatMap(super.insert)
        }
    }
  }

  override def findByProfileId(pid: String)(implicit ec: ExecutionContext) = find(Json.obj("profileId" -> pid))
}

/**
 * @author alari
 * @since 2/10/14
 */
trait UserIdentityDAO {
  def findByIdentityId(id: IdentityId)(implicit ec: ExecutionContext): Future[Option[UserIdentity]]

  def findByEmailAndProviderId(email: String, providerId: String)(implicit ec: ExecutionContext): Future[Option[UserIdentity]]

  def upsert(user: UserIdentity)(implicit ec: ExecutionContext): Future[UserIdentity]

  def findByProfileId(pid: String)(implicit ec: ExecutionContext): Future[List[UserIdentity]]
}