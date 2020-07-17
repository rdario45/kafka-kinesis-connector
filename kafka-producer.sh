#!/bin/bash
while getopts "b:t:" opt; do
  case ${opt} in
    b ) server=$OPTARG ;;
    t ) topic=$OPTARG ;;
    ? ) echo "Usage: $0 [-b broker] [-t topic]" 1>&2 ;;
  esac
done
printf "kafka-console-producer.sh --bootstrap-server %s --topic %s \n" $server $topic
exec kafka-console-producer.sh --bootstrap-server $server --topic $topic
