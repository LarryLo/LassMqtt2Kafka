lazy val root = (project in file(".")).
  settings(
    name := "LASSProducer",
    version := "0.1",
    scalaVersion := "2.10.4"
  )
/*
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.1",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2",
  "org.apache.spark" % "spark-streaming_2.10" % "1.5.1",
  "org.apache.spark" % "spark-streaming-mqtt_2.10" % "1.5.1",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.1",
  "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.1.2"
)
*/


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1" % "provided",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.1",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2" % "provided",
  "org.apache.spark" % "spark-streaming_2.10" % "1.5.1" % "provided",
  "org.apache.spark" % "spark-streaming-mqtt_2.10" % "1.5.1",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.1",
  "org.elasticsearch" % "elasticsearch-spark_2.10" % "2.1.2"
)
mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}

assemblyJarName in assembly := "Lass.jar"