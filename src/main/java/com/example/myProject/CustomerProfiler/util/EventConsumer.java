package com.example.myProject.CustomerProfiler.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.example.myProject.CustomerProfiler.domain.FinancialAction;

import java.util.Properties;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Kafka 에서는 Producer가 메시지를 보낼 때 각 메시지를 특정 파티션에 할당하기 위해 키를 기반으로 하는 해시 메커니즘을 사용합니다.
 * 즉 customerID 와 같은 키가 주어지면, Kafka는 이 키를 해싱하여 결과값을 통해 메시지가 저장될 파티션을 결정합니다.
 * 이러한 방식으로 동일한 'customerID'를 가진 메시지는 항상 동일한 파티션에 할당되어 순서를 유지할 수 있습니다.
 * 
 * 그러나 전체 customerID의 수(50,000)에 비해 파티션의 수가 적은 경우(10)개 여러키가 동일한 파티션에 매핑됩니다. 
 */

public class EventConsumer {
    private final int consumerCount = 5;
    private final String[] topics;
    private Properties props = new Properties();

    public EventConsumer() {
        // Consumer 설정
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // EOS(Exactly Once Semantics) 설정
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        this.topics = Arrays.stream(FinancialAction.values())
                    .map(Enum::name)
                    .toArray(String[]::new);
    }

    public void consumeEvent() {
        ExecutorService executor = Executors.newFixedThreadPool(topics.length * 5);                 

        // 토픽별 컨슈머 그룹 및 컨슈머 개수 할당
        for (String topic : topics) {
            for(int i =0; i< consumerCount; i++){
                String groupId = "group_" + topic + "_" + i;
                ConsumerRunner consumerRunner = new ConsumerRunner(topic, groupId, props);
                executor.submit(consumerRunner);
            }
        }
    }
}



/*
 *   a. 평가항목
        1) Kafka Consumer를 올바르게 사용했는지 여부
        2) 동시성 프로그래밍 기반으로 효율적으로 고객 프로파일 데이터를 생성 했는지 여부
        3) OOP 원칙에 맞게 객체 모델링을 했는지 여부
    b. 요구사항
        1) 메모리 기반 자료구조에 다음과 같은 데이터를 저장한다.
            - 고객
               1. 고객명
               2. 생년월일
               3. 가입일시
               4. 누적 세션 횟수
            - 계좌  
               1. 잔액
               2. 거래유형(입금, 출금, 이체)별 최소, 최대 거래금액
               3. 거래유형 구분없이 최근 3건의 거래내역
        2) 특정 고객의 데이터를 JSON 포맷으로 조회 가능한 명령을 REST API로 제공한다.
        3) 성능을 최적화한 아키텍처로 설계한다.
            - 실시간성 (이벤트 발생시각으로 부터 얼마나 빨리 프로파일 되는지)
            - 프로파일 처리량
            - CPU 부하
            - 메모리 사용량
 */