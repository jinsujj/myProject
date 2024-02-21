package com.example.myProject;
import java.util.Properties;

public class App 
{
    public static void main( String[] args )
    {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094"); 
        props.put("acks","all");
        props.put("retries",3);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 키 직렬화
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"); // 값 직렬화
    }

    /* 
     금융거래 로그는 세션시작, 가입, 계좌개설, 입금, 출금, 이체 라는 6가지 유형이 있다.     
     세션시작 로그는 고객번호와 세션시각으로 구성된다.
     가입로그는 고객번호와, 고객명, 생년월일, 가입 시각으로 구성된다.
     계좌개설로그는 고객번호, 계좌번호, 계좌개설 기각으로 구성된다.
     입금로그는 고객번호, 입금 계좌번호, 입금액, 입금시각으로 구성된다.
     출금로그는 고객번호, 출금 계좌번호, 출금액, 출금시각으로 구성된다.
     이체로그는 고객번호, 송금 계좌번호, 수취 은행, 수취 계좌번호, 수취 계좌주, 이체 금액, 이체 시각으로 구성된다
     각 금융거래 로그는 json 포맷으로 저장되며 kafka 를 통해 전송된다.
     해당 포맷은 avro 로 관리된다
    */
    
   

}
