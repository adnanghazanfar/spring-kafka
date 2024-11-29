# Spring Boot with Kafka Consumer

## start kafka
docker-compose -f docker-compose-conduktor.yml up


## Commands for Producers/Consumers:

### Add a producer with default partitioner
./kafka-topics.sh --bootstrap-server localhost:19092 --topic first_topic --create --partitions 3

./kafka-console-producer.sh --bootstrap-server localhost:19092 --topic first_topic --property parse.key=true --property key.separator=:

./kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic first_topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --from-beginning
