import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import queue.{LassMqtt, LassKafka}

object Lass {
  def main (args: Array[String]){

    if (args.length > 0) {

      if (args(0) == "LASSProducer") {
        println(args(0))
        val conf = new SparkConf().setAppName("LASSProducer").setMaster("spark://master1:7077")
        val ssc = new StreamingContext(conf, Seconds(10))

        //     LassFile.receiver(ssc)
        //     LassNc.receiver(ssc)
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

    } else {
      println("You have to input at least a Class name as parameters")
    }
  }
}
