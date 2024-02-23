# myProject

## Test 용 Kafka 실행
docker-compose -f docker-compose-infra.yml up -d 

### kafka-ui 
http://localhost:8989

## TestDataGenerator 파일 실행 
mvn exec:java -Dexec.args="generator"

## Profiler 파일 실행 
mvn exec:java -Dexec.args="profiler"




