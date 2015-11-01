lazy val root = (project in file(".")).
  settings(
    name := "spark-streaming",
    version := "0.1",
    scalaVersion := "2.10.6"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.5.1",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.2",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.10" % "2.1.2",
  "org.apache.spark" % "spark-streaming_2.10" % "1.5.1",
  "org.apache.spark" % "spark-streaming-mqtt_2.10" % "1.5.1",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.5.1"
)
