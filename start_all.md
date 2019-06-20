zookeeper/bin/zkServer.sh start
hadoop/sbin/start-dfs.sh
hbase/bin/hbase-daemon.sh start master
hbase/bin/regionservers.sh start
kafka/bin/kafka-server-start.sh kafka/config/server.properties &
kafka/kafka-manager-build/bin/kafka-manager  -Dconfig.file=conf/application.conf  -Dhttp.port=8888 &