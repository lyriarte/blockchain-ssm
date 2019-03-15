package org.civis.blockchain.ssm.client.query;

import org.civis.blockchain.ssm.client.command.InvokeArgs;

public interface HasList {

    String LIST_FUNCTION = "list";

    InvokeArgs listArgs();
}
