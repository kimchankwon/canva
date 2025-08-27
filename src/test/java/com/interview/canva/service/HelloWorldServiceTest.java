package com.interview.canva.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HelloWorldServiceTest {

    @InjectMocks
    private HelloWorldService helloWorldService;

    @Test
    public void testHelloWorld() {
        String result = helloWorldService.helloWorld();
        assertEquals("hello world", result);
    }
}
