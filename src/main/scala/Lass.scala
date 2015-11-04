import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import queue.{LassMqtt, LassKafka}

object Lass {
  def main (args: Array[String]){
    val conf = new SparkConf().setAppName("SparkKafka").setMaster("local[8]")
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
}
