package org.civis.blockchain.ssm.client.command;

import org.civis.blockchain.ssm.client.domain.Context;
import org.civis.blockchain.ssm.client.domain.Signer;

//{
//    "InvokeArgs": [
//        "perform",
//        "Sell",
//        "{\"session\":\"deal20181201\",\"public\":\"100 dollars 1978 Camaro\",\"iteration\":0}",
//        "sam",
//        "ANOVTS1mqW2M+u7IoR/A/S3lY2xnj7yS8fJg0k0XE3DeY+i23LzJL1ABrm/CxHbndqVtvmsDQ0pLJ/XGmdAxhpTSAj+oIi+bnQAxW5fAqtLt9KHOElnG7KWzO8xitHh7NaIDgMbMjxJ5tj8xRFB2OD69P6aqtv9sj6TqkIhWNCMYPzDl+/Rck3Su7/51heDeTkDjtPxnkyOYTnSTJF7eFaMTyauqAqtjQwznL9xhKIlxMcmLxmboEDNQd8tv3mT/8ALGhmo1YUWtMkgJ00li3NDhjq1+gVNjAcUpBhwN/N8lUmN6MElc9qwliHVOsWkwHAYvZ7r6Zdvf6typbFqkeA=="
//    ]
//}
//    echo "Usage: perform <action> <context> <signer>"
public class PerformCommand extends Command<Context> {

    private final static String COMMAND_NAME = "perform";

    private final String action;

    public PerformCommand(Signer signer, String action, Context context) {
        super(signer, COMMAND_NAME, context);
        this.action = action;
    }

    @Override
    protected String valueToSign(String json) throws Exception {
        return action+json;
    }

    protected InvokeArgs buildArgs(String command, String json, String signer, String b64Signature) throws Exception {
        return new InvokeArgs(command, action, json, signer, b64Signature);
    }

}
