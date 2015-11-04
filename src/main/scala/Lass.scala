import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import queue.{LassMqtt, LassKafka}

object Lass {

   def main (args: Array[String]){
     val conf = new SparkConf().setAppName("SparkKafka").setMaster("local[8]")
     val ssc = new StreamingContext(conf, Seconds(10))

//     LassFile.receiver(ssc)
//     LassNc.receiver(ssc)
     val dStream = new LassMqtt().receiver(ssc) // Have to change LassMqtt.scala.github to LassMqtt.scala
     dStream.foreachRDD( rddMsgs => {
       val msgs = rddMsgs.collect()
       msgs.foreach( msg => {
         new LassKafka().producer(msg)
       })
     })

     ssc.start()
     ssc.awaitTermination()
   }
 }
