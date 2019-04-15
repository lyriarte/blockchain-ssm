package org.civis.blockchain.ssm.client.query;

import org.civis.blockchain.ssm.client.command.InvokeArgs;

public abstract class AbstractQuery {

    public abstract String functionGetValue();

    public InvokeArgs queryArgs(String username) {
        return new InvokeArgs(functionGetValue(), username);
    }

    public InvokeArgs listArgs() {
        return new InvokeArgs(HasList.LIST_FUNCTION, functionGetValue());
    }
}
