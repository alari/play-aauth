package infra.aauth

import scala.concurrent.{ExecutionContext, Future}
import play.api.Plugin

/**
 * @author alari
 * @since 2/20/14
 */
trait ProfileService extends Plugin {
  def create(uid: UserIdentity)(implicit ec: ExecutionContext): Future[UserIdentity]
}

private[aauth] object ProfileService extends ProfileService {
  override def create(uid: UserIdentity)(implicit ec: ExecutionContext): Future[UserIdentity] = Future successful uid.copy(profileId = Some(uid.id))
}