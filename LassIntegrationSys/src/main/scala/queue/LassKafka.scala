package queue

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils
import org.slf4j.LoggerFactory
import parser.{KafkaConfigParser}
import collection.JavaConversions._

/**
 * Created by luoruhong on 2015/11/1.
 */
class LassKafka {
  val logger = LoggerFactory.getLogger(classOf[LassKafka])
  // Kakfa configuration
  val kafkaConfigs = KafkaConfigParser.readConfigs

  def producer() = new KafkaProducer[String, String](kafkaConfigs.get)
  def productRecord(message: String) = new ProducerRecord[String, String]("lass", message)

  def receiver(ssc: StreamingContext): DStream[String] = {
    val lines = KafkaUtils.createStream(ssc, "master1:2181", "0", Map("lass" -> 1), StorageLevel.MEMORY_AND_DISK_SER_2).map(_._2)
    val words = lines.flatMap(_.split(" "))
    words
  }
}