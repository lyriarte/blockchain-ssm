package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class Session implements HasPrivateMessage<Session> {
    private String ssm;
    private String session;
    private Map<String, String> roles;

    @JsonProperty("public")
    private String publicMessage;

    @JsonInclude(NON_NULL)
    @JsonProperty("private")
    private Map<String, String> privateMessage;

    public Session(@JsonProperty("ssm") String ssm, @JsonProperty("session") String session,
                   @JsonProperty("public") String publicMessage, @JsonProperty("roles") Map<String, String> roles,
                   @JsonProperty("private") Map<String, String> privateMessage) {
        this.ssm = ssm;
        this.session = session;
        this.publicMessage = publicMessage;
        this.roles = roles;
        this.privateMessage = privateMessage;
    }

    public Session(@JsonProperty("ssm") String ssm, @JsonProperty("session") String session,
                   @JsonProperty("public") String publicMessage, @JsonProperty("roles") Map<String, String> roles) {
       this(ssm, session, publicMessage, roles, null);
    }

    public String getSsm() {
        return ssm;
    }

    public String getSession() {
        return session;
    }

    public String getPublic() {
        return publicMessage;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    @Override
    public Map<String, String> getPrivate() {
        return privateMessage;
    }

    @Override
    public Session setPrivate(Map<String, String> privateMessage) {
        this.privateMessage = privateMessage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session1 = (Session) o;
        return Objects.equals(ssm, session1.ssm) &&
                Objects.equals(session, session1.session) &&
                Objects.equals(publicMessage, session1.publicMessage) &&
                Objects.equals(privateMessage, session1.privateMessage) &&
                Objects.equals(roles, session1.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssm, session, publicMessage, privateMessage, roles);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Session.class.getSimpleName() + "[", "]")
                .add("ssm='" + ssm + "'")
                .add("session='" + session + "'")
                .add("publicMessage='" + publicMessage + "'")
                .add("privateMessage=" + privateMessage)
                .add("roles=" + roles)
                .toString();
    }
}
