package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Context implements HasPrivateMessage<Context>{

    private String session;
    @JsonProperty("public")
    private String pub;

    @JsonInclude(NON_NULL)
    @JsonProperty("private")
    private Map<String, String> priv;
    private Integer iteration;

    @JsonCreator
    public Context(@JsonProperty("session") String session,
                   @JsonProperty("public") String pub,
                   @JsonProperty("iteration") Integer iteration) {
        this(session, pub, iteration, null);
    }

    @JsonCreator
    public Context(@JsonProperty("session") String session,
                   @JsonProperty("public") String pub,
                   @JsonProperty("iteration") Integer iteration,
                   @JsonProperty("private") Map<String, String> priv) {
        this.session = session;
        this.pub = pub;
        this.iteration = iteration;
        this.priv = priv;
    }

    public String getSession() {
        return session;
    }

    public Integer getIteration() {
        return iteration;
    }

    public String getPublic() {
        return pub;
    }

    public Context setPublic(String pub) {
        this.pub = pub;
        return this;
    }

    @Override
    public Map<String, String> getPrivate() {
        return priv;
    }

    @Override
    public Context setPrivate(Map<String, String> privateMessage) {
        this.priv = privateMessage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Context)) return false;
        Context context = (Context) o;
        return Objects.equals(session, context.session) &&
                Objects.equals(pub, context.pub) &&
                Objects.equals(priv, context.priv) &&
                Objects.equals(iteration, context.iteration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session, pub, priv, iteration);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Context.class.getSimpleName() + "[", "]")
                .add("session='" + session + "'")
                .add("pub='" + pub + "'")
                .add("priv=" + priv)
                .add("iteration=" + iteration)
                .toString();
    }
}
