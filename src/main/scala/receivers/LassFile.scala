import org.apache.spark.streaming.StreamingContext

/**
 * Created by luoruhong on 2015/10/20.
 */

object LassFile {

  def receiver(scc: StreamingContext) = {
    val logData = scc.textFileStream("/var/log/apache2")

    logData.print()
    scc.start()
    scc.awaitTermination()
  }
}
