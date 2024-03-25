package com.example.myproject.generator.util;

import java.util.Random;

public class RandomMaker {
    Random random = new Random();
    
    // 3자리의 한글 이름을 랜덤으로 생성
    public String generateName(){
        String[] firstName = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "임"};
        String[] middleName = {"민", "서", "영", "준", "현", "지", "진", "하", "승", "주"};
        String[] lastName = {"수", "진", "영", "민", "호", "희", "석", "미", "현", "기"};
        
        StringBuilder name = new StringBuilder();
        name.append(firstName[random.nextInt(firstName.length)]);
        name.append(middleName[random.nextInt(middleName.length)]);
        name.append(lastName[random.nextInt(lastName.length)]);
        
        return name.toString();
    }

    // 은행 이름을 랜덤으로 생성
    public String generateBankName(){
        String[] bankName = {"국민은행", "신한은행", "우리은행", "하나은행", "기업은행", "농협은행", "외환은행", "SC은행", "씨티은행", "대구은행","토스은행"};
        return bankName[random.nextInt(bankName.length)];
    }

    // 생년월일 (1950~2003)년 생 중 랜덤으로 생성
    public String generateBirth(){
        int year = random.nextInt((2003 - 1950) + 1) + 1950;
        int month = random.nextInt(12) + 1;
        int day;

        switch (month) {
            case 2:
                day = random.nextInt(28) + 1;
                break;
            case 4: case 6: case 9: case 11:
                day = random.nextInt(30) + 1;
                break;
            default:
                day = random.nextInt(31) + 1;
                break;
        }
        return String.format("%d-%02d-%02d", year, month, day);
    }
    
    // 3자리의 계좌번호를 랜덤으로 생성
    public String generateAccountNumber(){
        int part1 = random.nextInt(900) + 100;
        int part2 = random.nextInt(900) + 100;
        int part3 = random.nextInt(900) + 100;
        
        return String.format("%d-%d-%d", part1, part2, part3);
    }

    // 1000원 이상 100만원 이하의 금액을 랜덤으로 생성
    public long generateAmount(){
        return 1000 + random.nextInt(1000000);
    }
}   
    
