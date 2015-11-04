package queue

import org.apache.spark.streaming.StreamingContext

/**
 * Created by larry on 2015/10/20.
 */

object LassNc {

  def receiver(ssc: StreamingContext) = {
    val lines = ssc.socketTextStream("localhost", 7777)
//    transformation
    val infoLines = lines.flatMap( char => char.split(" "))
//    output
    infoLines.print()
    ssc.start()
    ssc.awaitTermination()

  }
}
