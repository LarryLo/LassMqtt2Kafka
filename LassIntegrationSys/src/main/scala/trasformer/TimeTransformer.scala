package trasformer

import java.text.SimpleDateFormat

object TimeTransformer {
  def toEsType(preDateTime: String) = dateTransform(preDateTime)

  private def dateTransform(preDate : String): String = {
    val preSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val preDatetime =  preSdf.parse(preDate)
    sdf.format(preDatetime)
  }
}
