package infra.aauth

/**
 * @author alari
 * @since 2/20/14
 */
trait UserRequired {
  def profileId: ProfileId

  def userId: String = profileId.id
}
