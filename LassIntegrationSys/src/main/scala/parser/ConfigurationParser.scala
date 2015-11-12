package parser

import org.slf4j.LoggerFactory
import scala.io.Source

/**
 * Created by luoruhong on 2015/11/1.
 */
trait TConfigurationParser {
  def readConfigs: Option[Map[String, Object]]
}

object KafkaConfigParser extends TConfigurationParser {
  val configName = "/kafka.properties"
  val logger = LoggerFactory.getLogger(KafkaConfigParser.getClass)

  override def readConfigs = {
    try {
      val kafkaConfigs = Source.fromURL(getClass.getResource(configName)).getLines().map( property => {
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

object ElasticConfigParser extends TConfigurationParser {
  val configName = "/elastic.properties"
  val logger = LoggerFactory.getLogger(ElasticConfigParser.getClass)

  override def readConfigs = {
    try {
      val elasticConfigs = Source.fromURL(getClass.getResource(configName)).getLines().map( property => {
        val pair = property.split("=")
        (pair(0), pair(1))
      }).toMap

      logger.info("Get Elastic properties successfully.")
      Some(elasticConfigs)
    } catch {
      case e: Exception => {
        logger.error(e.toString)
        None
      }
    }
  }
}