package com.group.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;


import static org.junit.Assert.*;

@Slf4j
public class RandomStringFakerTest {

    RandomStringFaker stringFaker = new RandomStringFaker();

    @Test
    public void testFaker(){
        for (int i = 0; i < 10; i++) {
            String firstStr = stringFaker.getFakeString();
            String secondStr = stringFaker.getFakeString();
            Assert.assertNotEquals(firstStr,secondStr);
        }
    }
}