#!/bin/bash
# entry point
KAFKA_HOME=/opt/kafka
JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# test paths
java -version
printf "Kafka version: %s \n" $(kafka-topics.sh --version)

# main loop.
counter=0
children=[]
while true; do
  ((++counter))
  children[0]=$(exec ./kafka-producer.sh $@)
#  echo "[json]:" && read exit
done
exit 0
