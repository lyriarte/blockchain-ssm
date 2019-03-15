package org.civis.blockchain.ssm.client.command;


import org.civis.blockchain.ssm.client.domain.Signer;
import org.civis.blockchain.ssm.client.domain.Agent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class RegisterCommandTest {

    @Test
    public void test_execute() throws Exception {
        Signer signer = Signer.loadFromFile("adam", "command/adam");
        Agent agent = Agent.loadFromFile("vivi", "command/vivi");

        InvokeArgs invokeArgs = new RegisterCommand(signer, agent).invoke();
        invokeArgs.getArgs().forEach(System.out::println);

        assertThat(invokeArgs.getFcn()).isEqualTo("register");
        assertThat(invokeArgs.getArgs())
                .isNotEmpty()
                .containsExactly(
                        "{\"name\":\"vivi\",\"pub\":\"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs2oFqOlrdpz/fEi5rQfEFWeWTeSXSLaaEwQAYof+EIQTYlvQ+1uk//dBFn+bPcp+BSdzgkra4jd0qsImVMgnrWIDUhs3vl2Wi9TgAQHXT/DtIbvlj+ZdPFTUzd3vb+8NR4i4ha8Yg9bbd5noaf3f40aJ1CY+huRV0/ElOFI5/hM00rZdxiFNcQ9NiA++osUzb4OZ5TqnePmwDpnI7qbE9mTOlbJju9JfmnppZv2HRkWRsdCPjKm+mKv5O9xR+Np5bSMTGqrVH0eyMleHrALEojDdfLt2FTf+ZiCCVKulV5jbMpKf3Qt7891vC5/QyDrtbEz7aPhU4FT1W2ks6rOLcwIDAQAB\"}",
                        "adam",
                        "oRz0NP6XnLLC6TnRZUDzHKPVlvDmgclArV1jknd6Z57mdnBAfStsxsLZmPg/+oOIXNX0sJQSh1bE1CyjJ4JQqNtYkEB9cNwnTJ26aGwMlNtnVLcWv80GxnMr0vso5W4CrX6Tv/lt/FW3dKShmVYjR0X8e96cFnZofbV4fEHsECDlgTKP242xhbz0n/tGAInlGnd8NFJBKfHEi7dEYZjUEbQDuRmMnWE12p9VbsS7EQHuxZx3vieqas9W1cPmiJZrYDDmQXUpzI5ucJ4VcJkuCJ54WWSF4XBXLp5yx3GxLjqGdPlymQ3cWiLpEfJZguSNwRa53p4hTyFU3HVyiCzCsw=="
                );

    }

}