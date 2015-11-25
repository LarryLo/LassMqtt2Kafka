package tester

import java.text.SimpleDateFormat
import java.util.concurrent.{Executors, ExecutorService}

import org.apache.kafka.clients.producer.KafkaProducer
import org.slf4j.LoggerFactory
import queue.LassKafka
import com.github.nscala_time.time.Imports._

/**
  * Created by luoruhong on 2015/11/23.
  */
class KafkaStressTester(val sleepTime: Long = 1000, val poolSize: Int = 2) {
  val message = "|ver_format=3|fmt_opt=0|app=PM25|ver_app=0.7.10|device_id=FT1_030|tick=236726786|device=LinkItONE|s_0=23710.00|s_1=100.00|s_2=1.00|s_3=0.00|s_d0=11.00|s_t0=24.60|s_h0=63.80|s_d1=13.00|gps_lat=25.000765|gps_lon=121.264969|gps_fix=0|gps_num=0|gps_alt=13"
//  val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)
  val kafka = new LassKafka()

  def submit() = {

    val producer = kafka.producer()
    while (true) {
      for (idx <- 0 to poolSize) {
        // MultiThread
//        pool.execute(new KafkaTesterProducer(producer))

        // SingleThread
        makeProducerRecord(producer)
      }
    }
    producer.close()
//    pool.shutdownNow()

  }

  def dateTransform(): (String, String) = {
    val nowDateTime = DateTime.now.toDate

    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateTimePair =  sdf.format(nowDateTime).split(" ")

    (dateTimePair(0), dateTimePair(1))
  }

  def makeProducerRecord(producer: KafkaProducer[String, String]): Unit = {
    val dateTimePair = dateTransform()
    val newMessage = message + "|date=" + dateTimePair._1 + "|time=" + dateTimePair._2
    println("\u001b[0;31m" + newMessage + "\u001b[m")
    producer.send(kafka.productRecord(newMessage))
    Thread sleep sleepTime
  }

  class KafkaTesterProducer(producer: KafkaProducer[String, String]) extends Runnable {
    override def run() = {
      makeProducerRecord(producer)
    }
  }

}
