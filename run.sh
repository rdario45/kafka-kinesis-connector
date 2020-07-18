#!/bin/bash
# entry point
WORKSPACE=/home/ruben.fernandez/workspace/offering-stream-consumer
KAFKA_HOME=/opt/kafka
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
# test paths
java -version
printf "Kafka version: %s\n" $(kafka-topics.sh --version)
printf "App workspace: %s\n" $WORKSPACE
# main app
while true; do
  exec ./kafka-console-consumer-interface.sh $@ && exit=$?
  read $exit
done
exit 0
