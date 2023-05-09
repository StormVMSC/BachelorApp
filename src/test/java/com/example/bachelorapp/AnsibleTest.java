package com.example.bachelorapp;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AnsibleTest{

    @Mock
    HttpSession session;

    @Mock
    CloseableHttpClient httpClient;

    @InjectMocks
    AnsibleAPIRepository ansibleRep;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetHostList() throws Exception {
        // Create mock objects
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        JsonNode rootNode = mock(JsonNode.class);
        JsonNode resultsNode = mock(JsonNode.class);

        // Set up mock objects to return appropriate values
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream("{\"results\": [{\"id\": 1, \"name\": \"host1\", \"summary_fields\": {\"last_job\": {\"finished\": \"2023-05-09T12:34:56.789Z\", \"status\": \"success\"}}}]}".getBytes()));
        
        List<Host> hostList = ansibleRep.getHostList(session);
        Host expectedHost = new Host(1, "host1", "09/05/2023", "success");
        assertEquals(1, hostList.size());
        assertEquals(expectedHost.getId(), hostList.get(0).getId());
        assertEquals(expectedHost.getName(), hostList.get(0).getName());
        assertEquals(expectedHost.getDate(), hostList.get(0).getDate());
        assertEquals(expectedHost.getStatus(), hostList.get(0).getStatus());

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
