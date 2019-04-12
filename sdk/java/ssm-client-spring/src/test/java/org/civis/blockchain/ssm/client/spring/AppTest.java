package org.civis.blockchain.ssm.client.spring;

import org.assertj.core.api.Assertions;
import org.civis.blockchain.ssm.client.SsmClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class AppTest {

    @Autowired
    private SsmClient ssmClient;


    @Test
    public void contextLoads() {
        Assertions.assertThat(ssmClient).isNotNull();
    }
}
