package tester

import java.util.concurrent.{Executors, ExecutorService}

import org.apache.kafka.clients.producer.KafkaProducer
import org.slf4j.LoggerFactory
import queue.LassKafka

/**
  * Created by luoruhong on 2015/11/23.
  */
class KafkaStressTester(val sleepTime: Long, val poolSize: Int) {
  val message = "|ver_format=3|fmt_opt=0|app=PM25|ver_app=0.7.6|device_id=FT1_007|tick=182448058|date=2015-11-13|time=14:32:48|device=LinkItONE|s_0=16993.00|s_1=100.00|s_2=1.00|s_3=0.00|s_d0=29.00|s_t0=30.00|s_h0=61.40|gps_lat=24.366204|gps_lon=120.433519|gps_fix=0|gps_num=0|gps_alt=13"

  val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)
  val kafka = new LassKafka()

  def submit() = {
    val producer = kafka.producer()
    try {
      println("Wait to submit LASS TStress Tester......")
      while (true) {
        for ( idx <- 0 to poolSize ) {
          pool.execute(new KafkaTesterProducer(producer))
        }
      }
    } catch {
      case e: Throwable => e.printStackTrace()
    } finally {
      pool.shutdownNow()
      producer.close()

    }
  }

  class KafkaTesterProducer(producer: KafkaProducer[String, String]) extends Runnable {
    override def run() = {
      println("\u001b[0;31m" + message + "\u001b[m")
      producer.send(kafka.productRecord(message))
      println("Run Thread: " + Thread.currentThread().getName + " successfully. Sleep 1 seconds...")
      Thread sleep sleepTime
    }
  }

}
