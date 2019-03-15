package org.civis.blockchain.ssm.client.command;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class InvokeArgs {

    private String fcn;

    private ArrayList<String> args;

    public InvokeArgs(String fcn, String... args) {
        this.fcn = fcn;
        this.args = Lists.newArrayList(args);
    }

    @JsonCreator
    public InvokeArgs(@JsonProperty("fcn") String fcn, @JsonProperty("args") List<String> args) {
        this.fcn = fcn;
        this.args = Lists.newArrayList(args);
    }

    public InvokeArgs(String function, Iterator<String> iterator) {
        this.fcn = function;
        this.args = Lists.newArrayList(iterator);
    }

    public String getFcn() {
        return fcn;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvokeArgs that = (InvokeArgs) o;
        return Objects.equals(fcn, that.fcn) &&
                Objects.equals(args, that.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fcn, args);
    }

    @Override
    public String toString() {
        return "InvokeArgs{" +
                "fcn='" + fcn + '\'' +
                ", args=" + args +
                '}';
    }
}
