package org.civis.blockchain.ssm.client.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;


//ssm:
//  name: docstampr-loop
//  coop:
//    url: http://localhost:9090
//  admin:
//    name: adam
//    file: file:./infra/dev/user/adam

@ConfigurationProperties(prefix = "ssm")
public class SsmConfiguration {

    private String name;
    private Coop coop;
    private Signer admin;

    public String getName() {
        return name;
    }

    public SsmConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public Signer getAdmin() {
        return admin;
    }

    public SsmConfiguration setAdmin(Signer admin) {
        this.admin = admin;
        return this;
    }

    public Coop getCoop() {
        return coop;
    }

    public SsmConfiguration setCoop(Coop coop) {
        this.coop = coop;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SsmConfiguration.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("admin=" + admin)
                .add("coop=" + coop)
                .toString();
    }

    public static class Signer {

        private String name;
        private String key;

        public String getName() {
            return name;
        }

        public Signer setName(String name) {
            this.name = name;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Signer setKey(String key) {
            this.key = key;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Signer.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("key='" + key + "'")
                    .toString();
        }
    }

    public static class Coop {

        private String url;
        private String key;

        public String getUrl() {
            return url;
        }

        public Coop setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Coop setKey(String key) {
            this.key = key;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Coop.class.getSimpleName() + "[", "]")
                    .add("url='" + url + "'")
                    .add("key='" + key + "'")
                    .toString();
        }
    }
}
