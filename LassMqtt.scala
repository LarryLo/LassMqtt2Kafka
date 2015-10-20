import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.mqtt.MQTTUtils

object LassMqtt {
  def receiver(ssc: StreamingContext) = {
    val lines = MQTTUtils.createStream(ssc, "tcp://<mqtt-host-ip>:1883", "<topic>", StorageLevel.MEMORY_ONLY_SER_2)
    val words = lines.flatMap( x => x)
    val param = words.map(x => x)
 //    val wordsCount = words.map( x => (x, 1))
     param.print()
//     words.print()
     ssc.start()
     ssc.awaitTermination()
   }
 }
