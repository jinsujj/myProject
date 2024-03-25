# myProject

## Test 용 Kafka 실행
```
docker-compose -f docker-compose-infra.yml up -d  
```

**kafka-ui** \
http://localhost:8989

**broker-1, broker-2, broker-3** \
http://localhost:9092, http://localhost:9093, http://localhost:9094

**Number of Partition** \
110

##  TestDataGenerator 파일 실행 
mvn exec:java -Dexec.args="generator"

##  Profiler 파일 실행 
mvn exec:java -Dexec.args="profiler"



##  프로젝트 구조 
```
.
├── README.md
├── docker-compose-infra.yml                              --  Kafka 브로커 및 kafka-ui 설정 파일 
├── pom.xml                                            
└── src
    └── main
        └── java
            └── com
                └── example
                    └── myproject
                        ├── App.java
                        ├── common
                        │   ├── domain                    --  도메인 객체
                        │   │   ├── Account.java
                        │   │   ├── Bank.java
                        │   │   ├── Customer.java
                        │   │   ├── FinancialAction.java
                        │   │   └── Transaction.java
                        │   └── response
                        │       ├── dto                   --- JavaSpark 에서 사용할 반환 DTO 객체
                        │       │   ├── AccountInfo.java
                        │       │   ├── BaseInfo.java
                        │       │   └── CustomerInfo.java
                        │       └── log                   --- Kafka 금융 로그 이벤트 스키마 객체
                        │           ├── AccountOpeningLog.java
                        │           ├── BaseLog.java
                        │           ├── DepositLog.java
                        │           ├── SessionStartLog.java
                        │           ├── SignUpLog.java
                        │           ├── TransferLog.java
                        │           └── WithdrawLog.java
                        ├── generator
                        │   ├── TestDataGenerator.java
                        │   └── util
                        │       ├── EventProducer.java    --- 카프카 프로듀서 설정 객체
                        │       ├── RandomMaker.java      --- 랜덤 데이터 생성 객체
                        │       └── SessionManager.java   --- 동일 고객 세션 관리 객체
                        └── profiler
                            ├── EventConsumer.java        --- 카프카 그룹 설정 및 관리 객체
                            ├── ConsumerRunner.java       --- 컨슈머 비동기 처리 객체
                            ├── ProcessorFactory.java     --- 이벤트 처리 추상화 객체
                            └── processor                 --- 이벤트 처리 구현부
                                ├── BaseProcessor.java    
                                ├── DepositProcessor.java
                                ├── OpenAccountProcessor.java
                                ├── SessionStartProcessor.java
                                ├── SignUpProcessor.java
                                ├── TransferProcessor.java
                                └── WithdrawalProcessor.java
```

제너레이터(프로듀서) 에서 '전체 고객 수'와, '동시 실행 인원 수', '랜덤 실행 간격' 를 조정 해 실행할 수 있도록 파라미터로 설정해두었으며, 고객 별 동시 세션 접속을 못하도록 구현했습니다. \
6가지 금융 로그는 각각의 financialLog 객체를 만들어, json 포맷으로 변환하기 쉽도록 스키마를 구현했습니다. 

프로파일러는 인터페이스와 추상 팩토리 패턴을 활용해서, 각 금융 로그별 처리 클래스를 구현하면서도 결합을 느슨하게 했습니다.


## 카프카 설정 관련
ETL 은 데이터의 누락, 중복이 모두 중요하기에, EOS(Exactly Once Sementatics) 설정을 해두었습니다. \
프로파일러는 순서도 중요하기에 단일 토픽을 사용했으며, 프로듀서에서 이벤트 발행 시, 고객id 별로 key 값을 두어 고객id 별로 특정 파티션에 쌓일 수 있도록 했습니다.  

컨슈머에서 제대로 commit 을 하지 못할 경우, 재시작 로직과 재시작 횟수 모두 소진 시 
임시 file 형태로 'topic', 'partition', 'offest' 정보를 저장해두도록 해뒀습니다.

실시간성을 고려하기 위해서 배치 commit을 고려하지는 않았습니다. \
그렇기에 컨슈머 개수는, (동시에 행동하는 고객 수 * 1.1) 개수를 두어서, 이벤트가 쌓이는 부하를 감당하도록 설계했습니다. \
또한 이렇게 돌리기 위해서 kafka broker 의 파티션 갯수도 컨슈머 수와 동일하게 하여 100% 성능을 끌어내도록 했습니다.


## 테스트 코드 관련 
> Kafka 서버 까지 모킹해서 테스트 코드를 구현하진 않았습니다.  \
카프카 서버가 실행되어 있는 상태에서 테스트 시 정상 동작 확인했습니다.


## Rest API 관련
프로파일러에서 프로그램에서 데이터를 조회하기 위한 API 로 3가지 정도를 생각해봤습니다.
- 고객 정보 조회 API 
  - GET /customers?page=1&size=100  
    - 요청 파라미터
      - page : 조회하려는 페이지 번호 default 1
      - size : 한 페이지당 표시할 고객 수 defalt 100
    - 응답 형식
      - totalCustomers: 전체 고객 수
      - customers: 현재 페이지에 해당하는 고객 정보 리스트
      - currentPage: 현재 페이지 번호
      - pageSize: 요청에 따라 반환된 페이지 크기(고객수)
    - 예시
        ```json
        {
            totalCustomers: "13115",
            pageSize: 500,
            customers: [
                {
                    customerNumber: "C1",
                    name: "최지영",
                    birthDate: "1984-09-02",
                    joinDateTime: "2024-02-27 01:59:53",
                    sessionCount: 21
                },
                {
                    customerNumber: "C32770",
                    name: "최지수",
                    birthDate: "1983-09-09",
                    joinDateTime: "2024-02-27 01:59:00",
                    sessionCount: 32
                },
                {
                    customerNumber: "C32771",
                    name: "김승석",
                    birthDate: "1990-07-22",
                    joinDateTime: "2024-02-27 02:00:38",
                    sessionCount: 12
                },
                {
                    customerNumber: "C3",
                    name: "김영희",
                    birthDate: "1959-09-20",
                    joinDateTime: "2024-02-27 02:01:27",
                    sessionCount: 22
                },
                ...
            ]
            
        ```

- 특정 고객 조회 API
  - GET /customer/:customerNumber
    - 파라미터 설명
      - customerNumber: 조회하려는 고객번호 ex) C101
    - 응답 형식
      - customerNumber  : 고객의 고유 번호
      - name            : 고객 명
      - birthDate       : 생년월일
      - joinDateTime    : 가입날짜
      - sessionCount    : 누적 세션 횟수
    - 예시
      ```json
        {
            customerNumber: "C1",
            name: "최지영",
            birthDate: "1984-09-02",
            joinDateTime: "2024-02-27 01:59:53",
            sessionCount: 7
        }
      ```

- 특정 고객 계좌 조회 API 
  - GET /customer/:customerNumber/account
    - 파라미터 설명
      - customerNumber: 조회하려는 고객번호 ex) C101
    - 응답 형식
      - accountNumber       : 계좌번호
      - balance             : 잔액
      - maxDepositAmount    : 최대 입금 금액
      - minDepositAmount    : 최소 입금 금액
      - maxWithdrawalAmount : 최대 인출 금액
      - minWithdrawalAmount : 최소 인출 금액
      - maxTransferAmount   : 최대 이체 금액
      - maxTransferAmount   : 최소 이체 금액
      - transactions        : 거래유형 구분없이 최근 3건의 거래내역
    - 예시
      ```json
        {
            accountNumber: "368-575-429",
            balance: 276932,
            maxDepositAmount: 279042,
            maxwithdrawalAmount: 0,
            maxTransferAmount: 19999,
            minDepositAmount: 17889,
            minWithdrawalAmount: 0,
            minTransferAmount: 19999,
            transactions: [
            {
                type: "DEPOSIT",
                amount: 279042,
                eventTime: "2024-02-27 02:02:33"
            },
            {
                type: "DEPOSIT",
                amount: 17889,
                eventTime: "2024-02-27 02:03:49"
            },
            {
                type: "TRANSFER",
                amount: 19999,
                eventTime: "2024-02-27 02:07:53",
                receivingBank: "토스은행",
                receivingAccountNumber: "796-363-827",
                receivingAccountHolder: "강진현"
            }
            ]
        }
      ```

## 고민 포인트
- OOP 설계 시, 해당 도메인 로직이 추가되면, 로직이 복잡해질 것이 예상되어, 상상해서 구현하기보다는 요구사항에 좀 더 집중했습니다. 
 
> - 1. 고객당 계좌 계수 \
  첫번째 세션에서만 계좌 개설을 하면 고객당 1개의 계좌만 가지나? \
  만약 여러개 계좌를 가진다면 어느 계좌로 (입금, 출금, 이체) 할지도 고려해야하나?

> - 2. 이체 관련 로직의 성공, 실패 여부 \
   다른 은행으로 이체 시, 해당 은행의 계좌가 없을 경우? \
   같은 은행내의 고객간에 이체 시 수취 계좌번호가 없을 경우? \
   같은 은행내의 고객간에 이체 시 수취 계좌번호와 수취 계죄주가 다를 경우?


## 개선 사항
- try catch 놓친 부분 확인
- 테스트 코드에서 set 을 넣은 부분
- 레이어드 아키텍처 
- 패키지명 이슈
- Immutable 하게 개선
