# LassMqtt2Kafka

##Project Name:

##Description:

- This is a experimental project for receiving Lass data generated by sensors produced via Mqtt to my local Kafka queue.

##Environment:

- Java: 1.7.0_79
- Scala: 2.10.4
- Spark: 1.5.1
- Kafka: 0.8.2.1
- Elasticsearch: 2.0
- Kibana: 4.2
- MongoDB: 2.0.8

##Basic Port:

- 2181: Zookeeper
- 7077: Spark
- 9092: Kafka
- 9200: Elasticsearch
- 5601: Kibana
- 27017: mongod

##Run Steps:

- Package all your class and dependencies of this project to a runnable jar by sbt

    $ sbt assembly

- Run Jar on spark standalone cluster
    // LASSProducer
    $ spark-submit --class Lass --master spark://10.1.81.151:7077 --executor-memory 2G --total-executor-cores 2 ~/Lass.jar LASSProducer

   // LASSxKibana
   $ spark-submit --class Lass --master spark://10.1.81.151:7077 --executor-memory 2G --total-executor-cores 2 ~/Lass.jar LASSxKibana
