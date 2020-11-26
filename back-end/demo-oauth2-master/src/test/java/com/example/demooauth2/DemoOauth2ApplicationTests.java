package com.example.demooauth2;

import com.example.demooauth2.controller.UserServiceImpTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UserServiceImpTest.class
})
public class DemoOauth2ApplicationTests {

    @Test
    public void contextLoads() {
    }

}
