package org.civis.blockchain.ssm.client.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

public class Session {
    private String ssm;
    private String session;
    private String publicMessage;
    private Map<String, String> roles;

    public Session(@JsonProperty("ssm") String ssm, @JsonProperty("session") String session,
                   @JsonProperty("public") String publicMessage, @JsonProperty("roles") Map<String, String>  roles) {
        this.ssm = ssm;
        this.session = session;
        this.publicMessage = publicMessage;
        this.roles = roles;
    }

    public String getSsm() {
        return ssm;
    }

    public String getSession() {
        return session;
    }

    @JsonProperty("public")
    public String getPublicMessage() {
        return publicMessage;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session1 = (Session) o;
        return Objects.equals(ssm, session1.ssm) &&
                Objects.equals(session, session1.session) &&
                Objects.equals(publicMessage, session1.publicMessage) &&
                Objects.equals(roles, session1.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ssm, session, publicMessage, roles);
    }

    @Override
    public String toString() {
        return "Session{" +
                "ssm='" + ssm + '\'' +
                ", session='" + session + '\'' +
                ", publicMessage='" + publicMessage + '\'' +
                ", roles=" + roles +
                '}';
    }
}
