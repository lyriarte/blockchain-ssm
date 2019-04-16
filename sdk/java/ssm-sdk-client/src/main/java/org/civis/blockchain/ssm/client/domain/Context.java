package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Context {

    private String session;
    @JsonProperty("public")
    private String pub;
    private Integer iteration;

    @JsonCreator
    public Context(@JsonProperty("session") String session, @JsonProperty("public") String pub,
                   @JsonProperty("iteration") Integer iteration) {
        this.session = session;
        this.pub = pub;
        this.iteration = iteration;
    }

    public String getSession() {
        return session;
    }

    public String getPub() {
        return pub;
    }

    public Integer getIteration() {
        return iteration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(session, context.session) &&
                Objects.equals(pub, context.pub) &&
                Objects.equals(iteration, context.iteration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, pub, iteration);
    }

    @Override
    public String toString() {
        return "Context{" +
                "session='" + session + '\'' +
                ", pub='" + pub + '\'' +
                ", iteration=" + iteration +
                '}';
    }
}
