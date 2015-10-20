lazy val root = (project in file(".")).
  settings(
    name := "spark-streaming",
    version := "0.1",
    scalaVersion := "2.10.6"
  )

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.0",
  "org.apache.spark" % "spark-streaming_2.10" % "1.3.0",
  "org.apache.spark" % "spark-streaming-mqtt_2.10" % "1.3.0",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.3.0"
)