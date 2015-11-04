package queue

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

  def producer(message: String) = {

    val producerRecord = new ProducerRecord[String, String]("LASS", "String")

    // Kakfa configuration
    val kafkaConfigs = ConfigurationParser.getKafkaConfigs()

    kafkaConfigs match {
      case Some(configs) => {
        val kafkaProducer = new KafkaProducer[String, String](configs)
        println("\u001b[0;31m" + message + "\u001b[m")
        try {
          kafkaProducer.send(new ProducerRecord[String, String]("LASS", message)).get()
        } catch {
          case e: Exception => logger.error(e.toString)
        }
//        var num = 1
//        while (true) {
//          try {
//            kafkaProducer.send(producerRecord).get()
//            println("Sent Msg: " + num + ". " + producerRecord.value())
//          } catch {
//            case e: Exception => println(e.toString)
//          }
//          num += 1
//        }

        kafkaProducer.close()
      }

      case None => logger.error("Fail to get Kafka properties.")

    }

  }
}