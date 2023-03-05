package com.guoran;

import com.guoran.domain.Consumer;
import com.guoran.service.ConsumerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConsumer {
    @Autowired
    private ConsumerService consumerService;
    @Test
    public void TestAdd() {
        Consumer consumer = new Consumer();
        consumer.setUsername("test");
        consumer.setPassword("123");
        consumer.setSex(new Byte("0"));
        consumer.setPhoneNum("15666412237");
        consumer.setEmail("1239679@qq.com");
        consumer.setBirth(new Date());
        consumer.setIntroduction("");
        consumer.setLocation("");
        consumer.setAvator("");
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());
        consumerService.addUser(consumer);
    }
}
