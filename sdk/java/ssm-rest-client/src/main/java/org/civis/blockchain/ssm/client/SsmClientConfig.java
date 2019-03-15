package org.civis.blockchain.ssm.client;

import org.civis.blockchain.ssm.client.Utils.FileUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class SsmClientConfig {

    private final static String SSM_REST_URL = "ssm.rest.url";

    public static SsmClientConfig fromConfigFile(String filename) throws IOException {
        URL file = FileUtils.getUrl(filename);
        Properties props = new Properties();
        props.load(new FileInputStream(file.getFile()));
        String baseUrl = props.getProperty(SSM_REST_URL);
        return new SsmClientConfig(baseUrl);
    }

    private final String baseUrl;

    public SsmClientConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
