import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Lass {

   def main (args: Array[String]){
     val conf = new SparkConf().setAppName("SparkKafka").setMaster("local[8]")//.setMaster("spark://192.168.0.7:7077")
     val ssc = new StreamingContext(conf, Seconds(5))

     LassMqtt.receiver(ssc)

   }
 }
