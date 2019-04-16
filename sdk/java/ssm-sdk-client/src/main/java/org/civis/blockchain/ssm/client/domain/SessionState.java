package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class SessionState extends Session {

    private Integer iteration;
    private Integer current;
    private Ssm.Transition origin;

    @JsonCreator
    public SessionState(@JsonProperty("ssm") String ssm, @JsonProperty("session") String session,
                        @JsonProperty("public") String publicMessage, @JsonProperty("roles") Map<String, String> roles,
                        @JsonProperty("origin") Ssm.Transition origin, @JsonProperty("current") Integer current,
                        @JsonProperty("iteration") Integer iteration) {
        super(ssm, session, publicMessage, roles);
        this.iteration = iteration;
        this.current = current;
        this.origin = origin;
    }

    public Integer getCurrent() {
        return current;
    }

    public Ssm.Transition getOrigin() {
        return origin;
    }

    public Integer getIteration() {
        return iteration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionState)) return false;
        if (!super.equals(o)) return false;
        SessionState that = (SessionState) o;
        return Objects.equals(iteration, that.iteration) &&
                Objects.equals(current, that.current) &&
                Objects.equals(origin, that.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), iteration, current, origin);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SessionState.class.getSimpleName() + "[", "]")
                .add("iteration=" + iteration)
                .add("current=" + current)
                .add("origin=" + origin)
                .toString();
    }
}
