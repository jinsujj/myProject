# myProject

## Test 용 Kafka 실행
docker-compose -f docker-compose-infra.yml up -d 

### kafka-ui 
http://localhost:8989

## TestDataGenerator 파일 실행 
mvn exec:java -Dexec.args="generator"

## Profiler 파일 실행 
mvn exec:java -Dexec.args="profiler"



#### 고민 포인트
- - 해당 도메인 로직이 추가되면, 로직이 복잡해질 것이 예상되어, 요구사항에 좀 더 집중했습니다. 
  
- 고객당 계좌를 여러개 가질 수 있어야 하지 않을까?
- - - 첫번째 세션에서만 계좌 개설을 하면 고객당 1개의 계좌만 가지나?
- - - 만약 여러개 계좌를 가진다면 어느 계좌로 (입금, 출금, 이체) 할지도 고려해야하나?

- 이체 관련 로직의 성공, 실패 여부도 고민해야 할까?
- - - 다른 은행으로 이체 시, 해당 은행의 계좌가 없을 경우?
- - - 같은 은행내의 고객간에 이체 시 수취 계좌번호가 없을 경우?
- - - 같은 은행내의 고객간에 이체 시 수취 계좌번호와 수취 계죄주가 다를 경우?
