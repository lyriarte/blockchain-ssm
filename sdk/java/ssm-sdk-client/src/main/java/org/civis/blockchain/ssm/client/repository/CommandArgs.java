package org.civis.blockchain.ssm.client.repository;

import com.google.common.base.MoreObjects;
import org.civis.blockchain.ssm.client.command.InvokeArgs;

import java.util.List;
import java.util.Objects;

public class CommandArgs {

    private String cmd;
    private String fcn;
    private List<String> args;

    public static CommandArgs from(String cmd, InvokeArgs invokeArgs) {
        return new CommandArgs()
                .setCmd(cmd)
                .setFcn(invokeArgs.getFcn())
                .setArgs(invokeArgs.getArgs());
    }

    public String getCmd() {
        return cmd;
    }

    public CommandArgs setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public String getFcn() {
        return fcn;
    }

    public CommandArgs setFcn(String fcn) {
        this.fcn = fcn;
        return this;
    }

    public List<String> getArgs() {
        return args;
    }

    public CommandArgs setArgs(List<String> args) {
        this.args = args;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandArgs)) return false;
        CommandArgs commandArgs = (CommandArgs) o;
        return Objects.equals(cmd, commandArgs.cmd) &&
                Objects.equals(fcn, commandArgs.fcn) &&
                Objects.equals(args, commandArgs.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cmd, fcn, args);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cmd", cmd)
                .add("fcn", fcn)
                .add("args", args)
                .toString();
    }
}
