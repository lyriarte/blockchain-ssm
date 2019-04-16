package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Ssm {

    private String name;
    private List<Transition> transitions;

    @JsonCreator
    public Ssm(@JsonProperty("name") String name, @JsonProperty("transitions")  List<Transition> transitions) {
        this.name = name;
        this.transitions = transitions;
    }

    public String getName() {
        return name;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public static class Transition {
        private Integer from;
        private Integer to;
        private String role;
        private String action;

        @JsonCreator
        public Transition(@JsonProperty("from")Integer from, @JsonProperty("tp") Integer to,
                          @JsonProperty("role")String role, @JsonProperty("action")String action) {
            this.from = from;
            this.to = to;
            this.role = role;
            this.action = action;
        }

        public Integer getFrom() {
            return from;
        }

        public Integer getTo() {
            return to;
        }

        public String getRole() {
            return role;
        }

        public String getAction() {
            return action;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Transition that = (Transition) o;
            return Objects.equals(from, that.from) &&
                    Objects.equals(to, that.to) &&
                    Objects.equals(role, that.role) &&
                    Objects.equals(action, that.action);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to, role, action);
        }

        @Override
        public String toString() {
            return "Transition{" +
                    "from='" + from + '\'' +
                    ", to='" + to + '\'' +
                    ", role='" + role + '\'' +
                    ", action='" + action + '\'' +
                    '}';
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ssm ssm = (Ssm) o;
        return Objects.equals(name, ssm.name) &&
                Objects.equals(transitions, ssm.transitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transitions);
    }

    @Override
    public String toString() {
        return "Ssm{" +
                "name='" + name + '\'' +
                ", transitions=" + transitions +
                '}';
    }
}
