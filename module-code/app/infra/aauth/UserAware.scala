package infra.aauth

/**
 * @author alari
 * @since 2/20/14
 */
trait UserAware {
  def profileIdOpt: Option[ProfileId]

  def userIdOpt: Option[String] = profileIdOpt.map(_.id)
}
