# Spring Boot with Kafka

### Start kafka
docker-compose -f docker-compose-conduktor.yml up

### Prerequisites
> * JDK 21
> * Apache Maven
> * Git

### How to start local version
- Start producer
```bash
cd spring-boot-kafka-producer
mvn clean spring-boot:run
```
- Start consumer
```bash
cd spring-boot-kafka-consumer
mvn clean spring-boot:run
```

### Produce messages
```bash
curl --location 'http://localhost:8081/producer/messages' \
--header 'Content-Type: application/json' \
--data '{
    "key": "K1",
    "value": "first message"
}'
```

```bash
curl --location 'http://localhost:8081/producer/messages' \
--header 'Content-Type: application/json' \
--data '{
    "key": "K2",
    "value": "second message"
}'
```

### Kafka commands to create topic/producers/consumers using cli

**Commands for Topics**
```bash

./kafka-topics.sh --bootstrap-server localhost:19092 --list

# Delete a topic
./kafka-topics.sh --bootstrap-server localhost:19092 --topic first_topic --delete

# Create a topic
./kafka-topics.sh --bootstrap-server localhost:19092 --topic first_topic –create

# Describe a topic
./kafka-topics.sh --bootstrap-server localhost:19092 --topic first_topic –describe

# Create a topic with partions
./kafka-topics.sh --bootstrap-server localhost:19092 --topic first_topic --create --partitions 3

# Create a topic with partitions and replications
./kafka-topics.sh --bootstrap-server localhost:19092 --topic third_topic --create --partitions 3 --replication-factor 1
```

**Commands for Producers/Consumers**
```bash
# Add a producer with default partitioner 
./kafka-console-producer.sh --bootstrap-server localhost:19092 --topic first_topic --property parse.key=true --property key.separator=:

# Add a producer with round robin partitioner 
./kafka-console-producer.sh --bootstrap-server localhost:19092 --producer-property partitioner.class=org.apache.kafka.clients.producer.RoundRobinPartitioner --topic second_topic 
```

**Commands for Consumer groups**
```bash
# Describe a Consumer Group
./kafka-consumer-groups.sh --bootstrap-server localhost:19092 --describe --all-groups –state

# describe one specific group
./kafka-consumer-groups.sh --bootstrap-server localhost:19092 --describe --group my-second-application

# describe a consumer group
./kafka-consumer-groups.sh --bootstrap-server localhost:19092 --group my-first-application -delete

# Consumer Group: RangeAssignor
./kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic second_topic --group my-first-application --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --from-beginning

# Consumer Group: RoundRobinAssignor
./kafka-console-consumer.sh --bootstrap-server localhost:19092 --topic second_topic --group my-first-application --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --property print.partition=true --consumer-property partition.assignment.strategy=org.apache.kafka.clients.consumer.RoundRobinAssignor --from-beginning
```

>
AG 2024