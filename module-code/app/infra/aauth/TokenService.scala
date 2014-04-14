package infra.aauth

import securesocial.core.providers.Token
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.Play.current
import reactivemongo.api.indexes.{Index, IndexType}
import play.api.libs.json.Json
import scala.concurrent.Await
import org.joda.time.DateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Plugin

/**
 * @author alari
 * @since 2/20/14
 */
trait TokenService extends Plugin {
  def save(token: Token)

  def findToken(token: String): Option[Token]

  def deleteToken(uuid: String)

  def deleteExpiredTokens()
}

private[aauth] object TokenService extends TokenService {
  private def db = ReactiveMongoPlugin.db

  private def collection: JSONCollection = db.collection[JSONCollection]("user.tokens")

  override def onStart() {
    collection.indexesManager.ensure(Index(Seq("uuid" -> IndexType.Descending), unique = true, dropDups = true))
    collection.indexesManager.ensure(Index(Seq("expirationDate" -> IndexType.Descending)))
  }

  implicit val tokenFormat = Json.format[Token]

  def save(token: Token) {
    collection.insert(token)
  }

  def findToken(token: String): Option[Token] = {
    Await.result(collection.find(Json.obj("uuid" -> token)).one[Token], AuthPlugins.Timeout)
  }

  def deleteToken(uuid: String) {
    collection.remove(Json.obj("uuid" -> uuid))
  }

  def deleteTokens() {
    collection.drop()
  }

  def deleteExpiredTokens() {
    collection.remove(Json.obj("expirationTime" -> Json.obj("$lte" -> DateTime.now())))
  }
}