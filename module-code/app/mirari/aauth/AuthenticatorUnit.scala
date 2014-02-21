package mirari.aauth

import securesocial.core.{IdentityId, Authenticator}
import mirari.mongo.{MongoDAO, MongoDomain}
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext


case class AuthenticatorUnit(_id: MongoDomain.Str.Id, value: Authenticator) extends MongoDomain.Str

object AuthenticatorUnit extends MongoDAO.Str[AuthenticatorUnit]("authenticators") {
   implicit val identityFormat = Json.format[IdentityId]
   implicit val authFormat = Json.format[Authenticator]
   implicit val format = Json.format[AuthenticatorUnit]

   def find(authId: String) = getById(authId).map(Some.apply) recover {
     case _ => None
   }

   def insert(unit: Authenticator) = {
     super.insert(AuthenticatorUnit(Some(unit.id), unit))
   }
 }