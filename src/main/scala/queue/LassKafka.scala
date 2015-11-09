package queue

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory
import parser.ConfigurationParser
import collection.JavaConversions._

/**
 * Created by luoruhong on 2015/11/1.
 */
class LassKafka {
  val logger = LoggerFactory.getLogger(classOf[LassKafka])
  // Kakfa configuration
  val kafkaConfigs = ConfigurationParser.getKafkaConfigs()

  def producer() = new KafkaProducer[String, String](kafkaConfigs.get)
  def productRecord(message: String) = new ProducerRecord[String, String]("lass", message)
}