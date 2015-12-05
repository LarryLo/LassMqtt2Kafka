package trasformer

object GeoTransformer {

  implicit def stringToFloat(str: String) = str.toFloat

  def toEsType(latitude: String)(longitude: String) = geoTransform(latitude, longitude)

  private def geoTransform(latitude: Float, longitude: Float): String = {
    val lat_m = (latitude - latitude.toInt) / 60 * 100 * 100
    val lon_m = (longitude - longitude.toInt) / 60 * 100 * 100
    val lat_s = (lat_m - lat_m.toInt) * 100
    val lon_s = (lon_m - lon_m.toInt) * 100
    val gps_lat = latitude.toInt + (lat_m.toInt).toFloat / 100 + lat_s / 10000
    val gps_lon = longitude.toInt + (lon_m.toInt).toFloat / 100 + lon_s / 10000

    s"$gps_lat,$gps_lon"
  }

}
