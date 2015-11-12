import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory
import queue.{LassMqtt, LassKafka}
import visualization.ElasticSearch

object Lass {
  def main (args: Array[String]){

    if (args.length > 0) {
      val logger = LoggerFactory.getLogger(Lass.getClass)
      logger.info("Start to run: " + args(0))

      args(0) match {
        case "LASSProducer" => {

          val conf = new SparkConf().setAppName("LASSProducer").setMaster("spark://master1:7077").set("spark.cores.max", "2")
          val ssc = new StreamingContext(conf, Seconds(10))

          val producer = new LassKafka().producer()
          val dStream = new LassMqtt().receiver(ssc)
          dStream.foreachRDD( rddMsgs => {
            val msgs = rddMsgs.collect()
            msgs.foreach( msg => {
              println("\u001b[0;31m" + msg + "\u001b[m")
              producer.send(new LassKafka().productRecord(msg))
            })
          })

          ssc.start()
          ssc.awaitTermination()
          producer.close()
        }

        case "LASSxKibana" => {

          val conf = new SparkConf().setAppName("LASSxKibana").setMaster("spark://master1:7077").set("spark.cores.max", "2")
          val es = new ElasticSearch().appendEsConfigs(conf)

          val ssc = new StreamingContext(conf, Seconds(10))
          val dStream = new LassKafka().receiver(ssc)
          dStream.foreachRDD( rddMsgs => {
            val allParams = rddMsgs.map { msg =>
              msg.split("""\|""").filterNot(_.isEmpty).map { param =>
                val pair = param.split("=")
                if (pair.length != 2 || pair(1).isEmpty) (pair(0), "0")
                else (pair(0), pair(1))
              }.toMap
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
}
