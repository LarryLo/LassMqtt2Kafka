package visualization

import org.apache.spark.rdd.RDD
import org.elasticsearch.spark._

/**
 * Created by luoruhong on 2015/11/10.
 */
class ElasticSearch {
  def saveToEs(rddMsgs: RDD[Map[String, String]]) = {
    rddMsgs.saveToEs("spark/docs")
  }

}
