package com.example.bachelorapp;

import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
class BachelorappApplicationTests {

    @Autowired
    private AnsibleAPIRepository APIRepository;

    @Test
    void contextLoads() {
    }

}
