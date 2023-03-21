package com.example.bachelorapp;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.io.IOException;
@Repository
public class AnsibleAPIRepository {

        private static final String url = "localhost:80";

        public static void job(String username, String password, int jobTemplateID) throws IOException {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(url);

                //authenticate login
                String auth = username + ":" + password;
                byte[] authBytes = Base64.encodeBase64(auth.getBytes());
                String authHeader = "Basic " + new String(authBytes);
                httpPost.setHeader("Authorization", authHeader);

                //add content-type header to the http request
                httpPost.setHeader("Content-Type", "application/json");

                //Execute the Http request and retrive response
                HttpResponse response = httpclient.execute(httpPost);
                HttpEntity entity = response.getEntity();

                if (entity != null){
                     String result = EntityUtils.toString(entity);
                     JSONObject jsonObject = new JSONObject(result);

                     Long jobId = jsonObject.getLong("id");
                        System.out.println("Ansible automation controller id: " + jobId);
                }
        }

        public static void main(String[] args) {


        }
}
