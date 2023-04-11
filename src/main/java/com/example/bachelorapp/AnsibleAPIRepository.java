package com.example.bachelorapp;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.ArrayList;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Repository;

import javax.net.ssl.*;
import java.io.IOException;

@Repository
public class AnsibleAPIRepository {
        private static final String url = "18.134.222.22";
        private static final String username = "admin";
        private static final String password = "redhat";
        public List<Host> getHostList() throws Exception {
                List<Host> hostList = new ArrayList<>();
                String authtoken = getAuthToken();
                SSLContext sslContext = null;
                try {
                        sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                                public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                }
                                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                }
                                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                }
                        } }, new SecureRandom());
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();
                HttpGet httpGet = new HttpGet("https://"+url+"/api/v2/hosts/");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + authtoken);
                CloseableHttpResponse response = httpClient.execute(httpGet);
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseString);
                JsonNode resultsNode = rootNode.get("results");
                for (JsonNode hostNode : resultsNode) {
                        int id = hostNode.get("id").asInt();
                        String hostName = hostNode.get("name").asText();
                        Host newHost = new Host(id, hostName);
                        hostList.add(newHost);
                }
                return hostList;
        }
        public String getAuthToken() throws IOException {

                String authToken = null;

                SSLContext sslContext = null;
                try {
                        sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, new TrustManager[] { new X509TrustManager() {
                                public X509Certificate[] getAcceptedIssuers() {
                                        return null;
                                }
                                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                }
                                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                }
                        } }, new SecureRandom());
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpPost httpPost = new HttpPost("https://" + url + "/api/v2/tokens/");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Accept", "application/json");


                // Set the credentials for basic authentication
                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
                httpPost.addHeader(BasicScheme.authenticate(creds, "UTF-8", false));

                // Execute the POST request
                CloseableHttpResponse response = httpClient.execute(httpPost);

                // If the reqeust was succsessful, extract the token from the response
                if (response.getStatusLine().getStatusCode() == 201) {
                        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode rootNode = mapper.readTree(responseString);
                        authToken = rootNode.get("token").asText();
                        return authToken;
                } else {
                        throw new RuntimeException(response.toString());
                }
        }
}


