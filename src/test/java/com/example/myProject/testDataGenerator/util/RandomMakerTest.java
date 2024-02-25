package com.example.myProject.testDataGenerator.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RandomMakerTest {
    
    RandomMaker randomMaker = new RandomMaker();

    @Test
    void testGenerateName(){
        // when
        String name = randomMaker.generateName();

        // then
        assertNotNull(name);
        assertTrue(name.length() == 3);
    }

    @Test
    void testGenerateBankName(){
        // when 
        String bankName = randomMaker.generateBankName();

        // then
        assertNotNull(bankName);
        assertTrue(bankName.contains("은행"));
    }

    @Test
    void testGenertateBirth(){
        // when
        String birth = randomMaker.generateBirth();
        int year = Integer.parseInt(birth.substring(0, 4));

        // then
        assertNotNull(birth);
        assertTrue(birth.matches("\\d{4}-\\d{2}-\\d{2}"));
        assertTrue(year >= 1950 && year <= 2003);
    }

    @Test
    void testGenerateAccountNumber(){
        // when 
        String accountNumber = randomMaker.generateAccountNumber();

        // then
        assertNotNull(accountNumber);
        assertTrue(accountNumber.matches("\\d{3}-\\d{3}-\\d{3}"));
    }

    @Test
    void testGenerateAmount(){
        // when 
        long amount = randomMaker.generateAmount();

        // then
        assertTrue(amount >= 1000 && amount <= 1000000);
    }
}
