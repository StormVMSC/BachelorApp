package com.example.bachelorapp;


import ch.qos.logback.core.encoder.NonClosableInputStream;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.*;
import java.io.ByteArrayInputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AnsibleTest {

    @Mock
    HttpSession session;

    @Mock
    CloseableHttpClient httpClient;
    @Mock
    HttpGet httpGet;
    @Mock
    HttpPost httpPost;
    @Mock
    SSLContext sslContext;
    @Mock
    Host host;
    @Mock
    Inventory inventory;
    @Mock
    Patch patch;
    @Mock
    Playbook playbook;
    @Mock
    Schedule schedule;


    @InjectMocks
    AnsibleAPIRepository ansibleRep;

    @Test
    void TestgetHostList() throws Exception {
        HttpSession session = mock(HttpSession.class);
        SSLContext sslContext = SSLContexts.createSystemDefault();
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent())
                .thenReturn(
                        new ByteArrayInputStream("{'results':[{'id':1,'name':'host1','summary_fields':{'last_job':{'finished':'2022-05-09T10:32:14Z','status':'successful'}}}]}".getBytes())
                );

    }

    private static SSLContext createSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }}, new SecureRandom());

        if (sslContext == null) {
            throw new NullPointerException("SSL_context er NULL!!");
        }

        return sslContext;
    }
}
