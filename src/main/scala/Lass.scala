import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import receivers.LassKafka

object Lass {

   def main (args: Array[String]){
     val conf = new SparkConf().setAppName("SparkKafka").setMaster("local[8]")
     val ssc = new StreamingContext(conf, Seconds(30))


//     LassFile.receiver(ssc)
//     LassNc.receiver(ssc)
     val dstream = new LassMqtt().receiver(ssc) // Have to change LassMqtt.scala.github to LassMqtt.scala
     new LassKafka().producer(dstream)

     ssc.start()
     ssc.awaitTermination()
   }
 }
