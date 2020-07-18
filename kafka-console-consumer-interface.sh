#!/bin/bash
printf "[INFO] %s %s@%s:/%s/%s\n" $(date --iso-8601=seconds) $USER $HOSTNAME $WORKSPACE $0
while getopts "b:t:" opt; do
  case ${opt} in
    b ) server=$OPTARG ;;
    t ) topic=$OPTARG ;;
    ? ) echo "Usage: $0 [-b broker] [-t topic]" 1>&2
        exit 1 ;;
  esac
done
printf "kafka-console-consumer-InTeRfAcE.sh --bootstrap-server %s --topic %s  --from-beginning\n" $server $topic
exec kafka-console-consumer.sh --bootstrap-server $server --topic $topic --from-beginning
printf "[INFO]\t\t\t%s %s@%s:/%s/%s\n" $(date --iso-8601=seconds) $USER $HOSTNAME $WORKSPACE $0
