package parser

import java.util.Properties
import org.slf4j.LoggerFactory
import scala.io.Source

/**
 * Created by luoruhong on 2015/11/1.
 */
object ConfigurationParser {
  val configName = "/kafka.properties"
  val logger = LoggerFactory.getLogger(ConfigurationParser.getClass)

  def getKafkaConfigs(): Option[Map[String, Object]] = {
    try {
      val properties = new Properties()
      val kafkaConfigs = Source.fromURL(getClass.getResource("/kafka.properties")).getLines().map( property => {
        val pair = property.split("=")
        (pair(0), pair(1))
      }).toMap
      
      logger.info("Get Kafka properties successfully.")
      Some(kafkaConfigs)
    } catch {
      case e: Exception => {
        logger.error(e.toString)
        None
      }
    }
  }
}