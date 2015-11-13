import java.text.SimpleDateFormat

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory
import queue.{LassMqtt, LassKafka}
import visualization.ElasticSearch
import com.github.nscala_time.time.Imports._

object Lass {
  def main (args: Array[String]){

    if (args.length > 0) {
      val logger = LoggerFactory.getLogger(Lass.getClass)
      logger.info("Start to run: " + args(0))

      args(0) match {
        case "LASSProducer" => {

          val conf = new SparkConf().setAppName("LASSProducer").setMaster("spark://lassy:7077").set("spark.cores.max", "2")
          val ssc = new StreamingContext(conf, Seconds(10))
          val kafka = new LassKafka()
          val producer = kafka.producer()

          val dStream = new LassMqtt().receiver(ssc)
          dStream.foreachRDD( rddMsgs => {
            val msgs = rddMsgs.collect()
            msgs.foreach( msg => {
              println("\u001b[0;31m" + msg + "\u001b[m")
              producer.send(kafka.productRecord(msg))
            })
          })

          ssc.start()
          ssc.awaitTermination()
          producer.close()
        }

        case "LASSxKibana" => {

          val conf = new SparkConf().setAppName("LASSxKibana").setMaster("spark://lassy:7077").set("spark.cores.max", "2")
          val es = new ElasticSearch().appendEsConfigs(conf)
          val preSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

          val ssc = new StreamingContext(conf, Seconds(10))
          val dStream = new LassKafka().receiver(ssc)
          dStream.foreachRDD( rddMsgs => {
            val allParams = rddMsgs.map { msg =>
              val parmMap = msg.split("""\|""").filterNot(_.isEmpty).map { param =>
                val pair = param.split("=")
                if (pair.length != 2 || pair(1).isEmpty) {
                  (pair(0), 0)
                } else {
                  (pair(0), toNewType(pair(1)))
                }

              }.toMap
              val preDatetime =  preSdf.parse((parmMap get "date").get.toString + " " + (parmMap get "time").get.toString)
              val datetime = sdf.format(preDatetime)
              parmMap + ("datetime" -> datetime)
            }

            val esParams = allParams.collect()
            es.saveToEs(esParams)

          })

          ssc.start()
          ssc.awaitTermination()
        }

        case _ => println("There is no " + args(0) + "function.")
      }


    } else {
      println("You have to input at least a Class name as parameters")
    }
  }

  def toNewType(value: String):Any = {
    try {
      value.toFloat
    } catch {
      case e: Exception => value
    }
  }
}
