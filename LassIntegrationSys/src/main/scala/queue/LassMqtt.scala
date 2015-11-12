package queue

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.mqtt.MQTTUtils

class LassMqtt {
  def receiver(ssc: StreamingContext): DStream[String] = {
    val lines = MQTTUtils.createStream(ssc, "tcp://gpssensor.ddns.net:1883", "LASS/Test/+", StorageLevel.MEMORY_ONLY_SER_2)
    val words = lines.flatMap( _.split(" "))
    words
   }
 }
