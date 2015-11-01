package receivers

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.streaming.dstream.DStream
import org.slf4j.LoggerFactory
import parser.ConfigurationParser
import collection.JavaConversions._

/**
 * Created by luoruhong on 2015/11/1.
 */
class LassKafka {
  val logger = LoggerFactory.getLogger(classOf[LassKafka])

  def producer(messages: DStream[String]) = {

    val producerRecord = new ProducerRecord[String, String]("LASS", "String")

    // Kakfa configuration
    val kafkaConfigs = ConfigurationParser.getKafkaConfigs()

    kafkaConfigs match {
      case Some(configs) =>
        val kafkaProducer = new KafkaProducer[String, String](configs)
        kafkaProducer.send(producerRecord)
        kafkaProducer.close()

      case None => logger.error("Fail to get Kafka properties.")

    }

  }
}
