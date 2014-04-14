package infra.aauth

import play.api._

import scala.concurrent.Await
import securesocial.core.{Identity, IdentityId, UserServicePlugin}
import play.api.cache.Cache
import AuthPlugins.{Timeout, userIdentityDAO, tokenService}
import securesocial.core.providers.Token
import play.api.libs.concurrent.Execution.Implicits.defaultContext


class AuthUserService(application: Application) extends UserServicePlugin(application) {
  implicit def app = application


  override def onStop() {
    try {
      super.onStop()
    } catch {
      case e: Throwable =>
        Logger.info("Cancelling", e)
    }
  }

  override def onStart() {
    tokenService.onStart()
    super.onStart()
  }

  def find(id: IdentityId): Option[UserIdentity] = {
    Cache.getOrElse(id.toString, 180) {
      Await.result(userIdentityDAO.findByIdentityId(id), Timeout)
    }
  }

  def findByEmailAndProvider(email: String, providerId: String): Option[UserIdentity] = {
    Await.result(userIdentityDAO.findByEmailAndProviderId(email, providerId), Timeout)
  }

  def save(user: Identity): UserIdentity = {
    Await.result(userIdentityDAO.upsert(user), Timeout)
  }

  def save(token: Token) {
    tokenService.save(token)
  }

  def findToken(token: String): Option[Token] = {
    tokenService.findToken(token)
  }

  def deleteToken(uuid: String) {
    tokenService.deleteToken(uuid)
  }

  def deleteExpiredTokens() {
    tokenService.deleteExpiredTokens()
  }
}