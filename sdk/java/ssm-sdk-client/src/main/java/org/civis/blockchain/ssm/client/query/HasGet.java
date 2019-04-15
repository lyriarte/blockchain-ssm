package org.civis.blockchain.ssm.client.query;

import org.civis.blockchain.ssm.client.command.InvokeArgs;

public interface HasGet {
    InvokeArgs queryArgs(String value);
}
