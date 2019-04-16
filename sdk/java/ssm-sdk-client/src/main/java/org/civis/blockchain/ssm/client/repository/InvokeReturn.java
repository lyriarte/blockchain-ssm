package org.civis.blockchain.ssm.client.repository;

import java.util.Objects;
import java.util.StringJoiner;

public class InvokeReturn {

    private String status;
    private String info;
    private String transactionId;

    public String getStatus() {
        return status;
    }

    public InvokeReturn setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public InvokeReturn setInfo(String info) {
        this.info = info;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public InvokeReturn setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvokeReturn)) return false;
        InvokeReturn that = (InvokeReturn) o;
        return Objects.equals(status, that.status) &&
                Objects.equals(info, that.info) &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, info, transactionId);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", InvokeReturn.class.getSimpleName() + "[", "]")
                .add("status='" + status + "'")
                .add("info='" + info + "'")
                .add("transactionId='" + transactionId + "'")
                .toString();
    }
}
