/opt/spark/sbin/stop-all.sh
service elasticsearch stop
ps aux | grep kibana | sed '/.*grep.*/d' | awk '{ print $2 }' | xargs kill -9
jps | grep Kafka | awk '{ print $1 }' | xargs kill -9
jps | grep QuorumPeerMain | awk '{ print $1 }' | xargs kill -9

