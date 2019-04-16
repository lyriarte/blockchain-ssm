package org.civis.blockchain.ssm.client.spring;

import org.civis.blockchain.ssm.client.domain.SignerAdmin;
import org.civis.blockchain.ssm.client.domain.Signer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.StringJoiner;


//ssm:
//  name: docstampr-loop
//  coop:
//    url: http://localhost:9090
//  signer:
//    admin:
//      name: adam
//      file: file:./infra/dev/user/adam

@ConfigurationProperties(prefix = "ssm")
public class SsmConfiguration {

    private String name;
    private Coop coop;
    private SignerConfig signer;

    public String getName() {
        return name;
    }

    public SsmConfiguration setName(String name) {
        this.name = name;
        return this;
    }

    public SignerConfig getSigner() {
        return signer;
    }

    public SsmConfiguration setSigner(SignerConfig signer) {
        this.signer = signer;
        return this;
    }

    public Coop getCoop() {
        return coop;
    }

    public SsmConfiguration setCoop(Coop coop) {
        this.coop = coop;
        return this;
    }

    public SignerAdmin adminSigner() throws Exception {
        if(signer== null|| signer.admin == null) {
            return null;
        }
        Signer sign = Signer.loadFromFile(signer.admin.getName(), signer.admin.getKey());
        return new SignerAdmin(sign);
    }



    @Override
    public String toString() {
        return new StringJoiner(", ", SsmConfiguration.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("signer=" + signer)
                .add("coop=" + coop)
                .toString();
    }

    public static class SignerConfig {

        private SignerUserConfig admin;

        public SignerUserConfig getAdmin() {
            return admin;
        }

        public SignerConfig setAdmin(SignerUserConfig admin) {
            this.admin = admin;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", SignerConfig.class.getSimpleName() + "[", "]")
                    .add("admin=" + admin)
                    .toString();
        }
    }

    public static class SignerUserConfig {

        private String name;
        private String key;

        public String getName() {
            return name;
        }

        public SignerUserConfig setName(String name) {
            this.name = name;
            return this;
        }

        public String getKey() {
            return key;
        }

        public SignerUserConfig setKey(String key) {
            this.key = key;
            return this;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", SignerConfig.class.getSimpleName() + "[", "]")
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
