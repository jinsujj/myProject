# myProject

## Test 용 Kafka 실행
```
docker-compose -f docker-compose-infra.yml up -d  
```

**kafka-ui** \
http://localhost:8989

**broker-1, broker-2, broker-3** \
http://localhost:9092, http://localhost:9093, http://localhost:9093

**Number of Partition** \
110

##  TestDataGenerator 파일 실행 
mvn exec:java -Dexec.args="generator"

##  Profiler 파일 실행 
mvn exec:java -Dexec.args="profiler"



##  프로젝트 구조 
```
├── docker-compose-infra.yml    --  Kafka 브로커 및 kafka-ui 설정 파일 
├── pom.xml
└── src
    └── main
        └── java
            └── com
                └── example
                    └── myProject
                        ├── App.java                               --- Generator, Profiler 구동 객체
                        ├── common
                        │   ├── domain                             --- 도메인 정의
                        │   │   ├── Account.java
                        │   │   ├── Bank.java
                        │   │   ├── Customer.java
                        │   │   ├── FinancialAction.java
                        │   │   └── Transaction.java
                        │   ├── financialLog                       --- 카프카 이벤트 스키마 객체
                        │   │   └── v1
                        │   │       ├── AccountOpeningLog.java
                        │   │       ├── DepositLog.java
                        │   │       ├── SessionStartLog.java
                        │   │       ├── SignUpLog.java
                        │   │       ├── TransferLog.java
                        │   │       └── WithdrawLog.java
                        │   └── responseDto                        --- JavaSpark 에서 사용할 반환 DTO 객체
                        │       ├── AccountInfo.java
                        │       └── CustomerInfo.java
                        ├── customerProfiler
                        │   ├── EventConsumer.java                 --- 카프카 그룹 설정 및 관리 객체
                        │   ├── ConsumerRunner.java                --- 컨슈머 비동기 처리 객체
                        │   ├── MessageProcessor.java              --- 이벤트 처리 인터페이스
                        │   ├── ProcessorFactory.java              --- 이벤트 처리 추상화 객체
                        │   └── messageProcessorImpl               --- 이벤트 처리 구현부
                        │       ├── DepositProcessor.java
                        │       ├── OpenAccountProcessor.java
                        │       ├── SessionStartProcessor.java
                        │       ├── SignUpProcessor.java
                        │       ├── TransferProcessor.java
                        │       └── WithdrawalProcessor.java
                        └── testDataGenerator
                            ├── TestDataGenerator.java             --- 테스트 데이터 생성 객체
                            └── util
                                ├── EventProducer.java             --- 카프카 프로듀서 설정 객체
                                ├── RandomMaker.java               --- 랜덤 데이터 생성 객체
                                └── SessionManager.java            --- 동일 고객 세션 관리 객체
```

## 카프카 설정 관련
ETL 은 데이터의 누락, 중복이 모두 중요하기에, EOS(Exactly Once Sementatics) 설정을 해두었습니다. 
프로파일러는 순서도 중요하기에 단일 토픽을 사용했으며, 프로듀서에서 고객id 별로 key 값을 두어 
고객id 별로 특정 파티션에 쌓일 수 있도록 했습니다.  

또한 컨슈머에서 제대로 commit 을 하지 못할 경우, 재시작 로직과 재시작 횟수 모두 소진 시 
임시 file 형태로 'topic', 'partition', 'offest' 정보를 저장해두도록 해뒀습니다.

컨슈머 개수는, (동시에 행동하는 고객 수 * 1.1) 개수를 두어서, 이벤트가 쌓이는 부하를 감당하도록 설계했습니다.
또한 이렇게 돌리기 위해서 kafka broker 의 파티션 갯수도 컨슈머 수와 동일하게 하여 100% 성능을 끌어내도록 했습니다.




## 테스트 코드 관련 
> Kafka 서버 까지 모킹해서 테스트 코드를 구현하진 않았습니다.  \
카프카 서버가 실행되어 있는 상태에서 테스트 시 정상 동작 확인했습니다.


## 고민 포인트
- OOP 설계 시, 해당 도메인 로직이 추가되면, 로직이 복잡해질 것이 예상되어, 요구사항에 좀 더 집중했습니다. 
 
> - 1. 고객당 계좌 계수 \
  첫번째 세션에서만 계좌 개설을 하면 고객당 1개의 계좌만 가지나? \
  만약 여러개 계좌를 가진다면 어느 계좌로 (입금, 출금, 이체) 할지도 고려해야하나?

> - 2. 이체 관련 로직의 성공, 실패 여부 \
   다른 은행으로 이체 시, 해당 은행의 계좌가 없을 경우? \
   같은 은행내의 고객간에 이체 시 수취 계좌번호가 없을 경우? \
   같은 은행내의 고객간에 이체 시 수취 계좌번호와 수취 계죄주가 다를 경우?

