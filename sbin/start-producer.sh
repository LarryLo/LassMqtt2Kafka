nohup /opt/spark/bin/spark-submit --total-executor-cores 2 --executor-memory 2G ~/Lass.jar LASSProducer > /var/log/lass-producer.log 2>&1 &
