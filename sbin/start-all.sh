nohup zookeeper-server-start.sh /opt/kafka/config/zookeeper.properties > /var/log/kafka-zookeeper.log 2>&1 &
nohup kafka-server-start.sh /opt/kafka/config/server.properties > /var/log/kafka-server.log 2>&1 &
/opt/spark/sbin/start-all.sh
service elasticsearch restart
nohup kibana > /var/log/kibana.log 2>&1 &