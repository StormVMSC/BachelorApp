package com.example.bachelorapp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;
import org.apache.http.client.methods.HttpDelete;
import org.json.JSONArray;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import javax.net.ssl.*;
import java.io.IOException;

@Repository
public class AnsibleAPIRepository {
        private static final String url = "18.134.222.22";

        // this method returns a list of Host objects by making an API call to Ansible
        public List<Host> getHostList(HttpSession session) throws Exception {
                // Create an empty ArrayList to store the host
                List<Host> hostList = new ArrayList<>();

                String authToken = (String) session.getAttribute("auth");
                // Get the Authorization token needed to authenticate the API


                // Create an SSL context to disable SSL
                SSLContext sslContext = null;
                try {
                     sslContext = createSSLContext();
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        e.printStackTrace();
                }

                //Create an HTTP cliens using the custom SSL context and ignore hostname authentication
                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                // Create an HTTP GET request to fetch the list of hosts
                HttpGet httpGet = new HttpGet("https://" + url + "/api/v2/hosts/");

                // Set headers for the request, including the authorization token
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + authToken);


                // Execute the request and get the response
                CloseableHttpResponse response = httpClient.execute(httpGet);

                // Convert the response to a String
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                // Parse the response String as a JSON object using an ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseString);
                // Get the "results" node from the JSON object
                JsonNode resultsNode = rootNode.get("results");

                // Loop through each host node in the results and add it to the hostList
                for (JsonNode hostNode : resultsNode) {
                        int id = hostNode.get("id").asInt();
                        String hostName = hostNode.get("name").asText();
                        JsonNode summary_fields = hostNode.get("summary_fields");
                        JsonNode last_job = summary_fields.get("last_job");
                        String date = last_job.get("finished").asText();
                        String status = last_job.get("status").asText();
                        
                        Host newHost = new Host(id, hostName, dateFormatter(date), status);
                        hostList.add(newHost);
                }
                return hostList;
        }

        public List<Inventory> getInventory(HttpSession session) throws IOException {
                List<Inventory> inventoryList = new ArrayList<>();

                String authToken = (String) session.getAttribute("auth");

                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();


                // Create an HTTP GET request to fetch the list of hosts
                HttpGet httpGet = new HttpGet("https://" + url + "/api/v2/inventories/");

                // Set headers for the request, including the authorization token
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + authToken);

                //Execute the request and get the response
                CloseableHttpResponse response = httpClient.execute(httpGet);

                // Convert the response to a String
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                // Parse the response String as a JSON object using an ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(responseString);
                // Get the "results" node from the JSON object
                JsonNode resultsNode = rootNode.get("results");

                // Loop through each host node in the results and add it to the hostList
                for (JsonNode inventoryNode : resultsNode) {
                        int id = inventoryNode.get("id").asInt();
                        String inventoryName = inventoryNode.get("name").asText();
                        Inventory newinventory = new Inventory(id, inventoryName);
                        inventoryList.add(newinventory);
                }
                return inventoryList;
        }

        //this methods gets token for performing api tasks
        public String getAuthToken(String username, String password) throws IOException {

                // Create a String variable for authentication token
                String authToken = null;

                // Create an SSL context to disable SSL
                SSLContext sslContext = null;
                try {
                  sslContext = createSSLContext();
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        e.printStackTrace();
                }

                //Create an HTTP cliens using the custom SSL context and ignore hostname authentication
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
                return sslContext;
        }


        public void configurate(Patch patchData, HttpSession session) throws IOException {

                String authToken = (String) session.getAttribute("auth");


                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpPost request = new HttpPost( "https://"+ url + "/api/v2/job_templates/"+ patchData.getJobId() +"/launch/");
                request.addHeader("content-type", "application/json");
                request.setHeader("Authorization", "Bearer " + authToken);

                String hostName = String.join(",", patchData.getAllName());
                JSONObject extraVars = new JSONObject();
                extraVars.put("host_name", hostName);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("extra_vars", extraVars);

                StringEntity entity = new StringEntity(jsonBody.toString());
                request.setEntity(entity);


                CloseableHttpResponse response = httpClient.execute(request);
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (responseString != null) {
                        System.out.println(responseString);
                }
        }


        public List<Historikk> getHistorikk(HttpSession session) throws IOException {
                List<Historikk> historikkList = new ArrayList<>();
                String authToken = (String) session.getAttribute("auth");

                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpGet httpGet = new HttpGet("https://" + url + "/api/v2/jobs/");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization", "Bearer " + authToken);

                CloseableHttpResponse response = httpClient.execute(httpGet);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());
                JsonNode resultsNode = jsonNode.get("results");

                for (JsonNode historikkNode : resultsNode) {
                        int id = historikkNode.get("id").asInt();
                        String navn = historikkNode.get("name").asText();
                        String status = historikkNode.get("status").asText();
                        String inventory = historikkNode.get("inventory").asText();
                        String startTime = historikkNode.get("started").asText();
                        String finnishTime =  historikkNode.get("finished").asText();
                        Historikk historikk = new Historikk(id, navn, status, inventory, dateFormatter(startTime), dateFormatter(finnishTime));
                        historikkList.add(historikk);
                }

                return historikkList;
        }

        public void schedulePatch(Schedule scheduleData, HttpSession session) throws IOException {
                String authToken = (String) session.getAttribute("auth");

                String username = "BrukerA";
                String passord = "PassordA";

                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpPost request = new HttpPost( "https://18.134.222.22/api/v2/schedules/");
                request.addHeader("content-type", "application/json");
                request.setHeader("Authorization","Bearer " + authToken);

                //String rrule = "DTSTART;TZID=Europe/Oslo:" + scheduleData.getDato() + "T" + scheduleData.getTid() + " RRULE:FREQ=" + scheduleData.getFrekvens() + ";INTERVAL=" + scheduleData.getIntervall();
                String hostName = String.join(",", scheduleData.getHosts());

                JSONObject extraVars = new JSONObject();
                extraVars.put("host_name", hostName);

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("extra_data", extraVars);
                jsonBody.put("unified_job_template", scheduleData.getPlaybookId());
                jsonBody.put("enabled", true);
                jsonBody.put("name", scheduleData.getNavn());
                jsonBody.put("rrule", scheduleData.getRrule());





                StringEntity entity = new StringEntity(jsonBody.toString());
                request.setEntity(entity);


                CloseableHttpResponse response = httpClient.execute(request);
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (responseString != null) {
                        System.out.println(responseString);
                }

        }

        public void deleteSchedule(int id, HttpSession session) throws IOException {

                String authToken = (String) session.getAttribute("auth");


                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpDelete request = new HttpDelete( "https://18.134.222.22/api/v2/schedules/" + id + "/");
                request.addHeader("content-type", "application/json");
                request.setHeader("Authorization","Bearer " + authToken);

                CloseableHttpResponse response = httpClient.execute(request);
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

                if (responseString != null) {
                        System.out.println(responseString);
                }
        }

        public List<Schedule> getSchedule(HttpSession session) throws IOException {
                List<Schedule> scheduleList = new ArrayList<>();

                String authToken = (String) session.getAttribute("auth");
                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpGet httpGet = new HttpGet("https://" + url + "/api/v2/schedules/");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization","Bearer " + authToken);

                CloseableHttpResponse response = httpClient.execute(httpGet);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());
                JsonNode resultsNode = jsonNode.get("results");


                for (JsonNode scheduleNode : resultsNode) {
                        int id = scheduleNode.get("id").asInt();
                        String navn = scheduleNode.get("name").asText();
                        String rrule = scheduleNode.get("rrule").asText();
                        String playbookId = scheduleNode.get("unified_job_template").asText();

                        JsonNode extraDataNode = scheduleNode.get("extra_data");
                        List<String> hosts = new ArrayList<>();
                        if (extraDataNode != null) {
                                hosts.add(extraDataNode.has("host_name") ? extraDataNode.get("host_name").asText() : "N/A");
                        }
                        else {
                                hosts.add("hmm");
                        }

                        Schedule schedule = new Schedule(id, navn, rrule, playbookId, hosts);
                        scheduleList.add(schedule);
                }
                return scheduleList;
        }

        public List<Playbook> getPlaybook(HttpSession session) throws IOException {
                List<Playbook> playbookList = new ArrayList<>();

                String authToken = (String) session.getAttribute("auth");

                String username = "BrukerA";
                String passord = "PassordA";

                SSLContext  sslContext = null;
                try{
                        sslContext = createSSLContext();
                }catch(NoSuchAlgorithmException | KeyManagementException e){
                        e.printStackTrace();
                }

                CloseableHttpClient httpClient = HttpClients.custom()
                        .setSSLContext(sslContext)
                        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                        .build();

                HttpGet httpGet = new HttpGet("https://" + url + "/api/v2/job_templates/");
                httpGet.setHeader("Content-type", "application/json");
                httpGet.setHeader("Authorization","Bearer " + authToken);

                CloseableHttpResponse response = httpClient.execute(httpGet);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());
                JsonNode resultsNode = jsonNode.get("results");


                for (JsonNode playbookNode : resultsNode) {
                        int id = playbookNode.get("id").asInt();
                        String navn = playbookNode.get("name").asText();

                        Playbook playbook = new Playbook(id, navn);
                        playbookList.add(playbook);
                }
                return playbookList;
        }

        private String dateFormatter(String date){
                Instant instant = Instant.parse(date);
                ZoneId zone = ZoneId.of("Europe/Oslo");
                ZonedDateTime zonedDateTime = instant.atZone(zone);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return zonedDateTime.format(formatter);
        }
}




