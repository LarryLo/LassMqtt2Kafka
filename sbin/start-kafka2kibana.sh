curl -X PUT -d '{"mappings":{"basic":{"properties":{"location":{"type":"geo_point"}}}}}' localhost:9200/lass-geo
nohup /opt/spark/bin/spark-submit --total-executor-cores 2 --executor-memory 2G ~/Lass.jar LASSxKibana > /var/log/lass-kibana.log 2>&1 &
