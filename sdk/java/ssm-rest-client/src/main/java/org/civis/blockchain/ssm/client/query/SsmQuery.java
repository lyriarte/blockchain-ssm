package org.civis.blockchain.ssm.client.query;

public class SsmQuery extends AbstractQuery implements HasGet, HasList {

    private static final String GET_FUNCTION = "ssm";

    @Override
    public String functionGetValue() {
        return GET_FUNCTION;
    }

}
