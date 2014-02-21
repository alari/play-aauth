package mirari.aauth

import securesocial.core.{Authenticator, AuthenticatorStore}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.Play.current
import play.api.cache.Cache
import play.api.libs.concurrent.Execution.Implicits.defaultContext


/**
 * @author alari
 * @since 11/15/13 5:19 PM
 */
class PersistentAuthenticatorStore(app: play.api.Application) extends AuthenticatorStore(app) {
  val CacheAuthenticatorTimeout = 1200

  def save(authenticator: Authenticator): Either[Error, Unit] = {
    Cache.set(authenticator.id, authenticator, CacheAuthenticatorTimeout)
    AuthenticatorUnit.insert(authenticator)

    Right(())
  }

  def find(id: String): Either[Error, Option[Authenticator]] = {
    Cache.getAs[Authenticator](id) match {
      case Some(a) => Right(Some(a))
      case None =>
        Await.result(AuthenticatorUnit.find(id).map(_.map(_.value)), Duration("1 second")) match {
          case Some(a) =>
            Cache.set(id, a, CacheAuthenticatorTimeout)
            Right(Some(a))
          case None =>
            Right(None)
        }
    }
  }

  def delete(id: String): Either[Error, Unit] = {
    Cache.remove(id)
    AuthenticatorUnit.remove(id)
    Right(())
  }
}
