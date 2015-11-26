package parser

import org.slf4j.LoggerFactory
import scala.io.Source

/**
 * Created by luoruhong on 2015/11/1.
 */


trait ConfigurationParser {
  def readConfigs: Option[Map[String, Object]]
}

object ConfigurationParser {
  private object KafkaConfigParser extends ConfigurationParser {

    val configName = "/kafka.properties"
    override def readConfigs: Option[Map[String, Object]] = transParams2Map(configName)
  }

  private object ElasticConfigParser extends ConfigurationParser {

    val configName = "/elasticsearch.properties"
    override def readConfigs: Option[Map[String, Object]] = transParams2Map(configName)

  }

  def apply(fileName: String): Option[Map[String, Object]] = {
    if (fileName == "kafka") KafkaConfigParser readConfigs
    else if (fileName == "elasticsearch") ElasticConfigParser readConfigs
    else None
  }

  def transParams2Map(configName: String): Option[Map[String, Object]] = {
    val logger = LoggerFactory.getLogger(this.getClass)

    try {
      val configs = Source.fromURL(this.getClass.getResource(configName)).getLines().map( property => {
        val pair = property.split("=")
        (pair(0), pair(1))
      }).toMap

      logger.info(s"Get $configName properties successfully.")
      Some(configs)

    } catch {
      case e: Exception => {
        logger.error(e.toString)
        None
      }
    }
  }
}

