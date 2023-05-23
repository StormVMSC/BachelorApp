package com.example.bachelorapp;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.message.BasicStatusLine;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.net.ssl.*;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void testGetHostListOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"host1\", " +
                        "\"summary_fields\": {" +
                        "\"last_job\": {" +
                        "\"finished\": \"2023-05-09T12:34:56.789Z\", " +
                        "\"status\": \"success\"" +
                        "}}}]}").getBytes()
                ));

        //kaller metoden for å bli testa på og lagre resultatene
        List<Host> hostList = ansibleRep.getHostList(session);

        //setter opp forventet variabler for hosten
        Host expectedHost = new Host(1, "host1", "09/05/2023", "success");

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, hostList.size());
        assertEquals(expectedHost.getId(), hostList.get(0).getId());
        assertEquals(expectedHost.getName(), hostList.get(0).getName());
        assertEquals(expectedHost.getDate(), hostList.get(0).getDate());
        assertEquals(expectedHost.getStatus(), hostList.get(0).getStatus());
    }

    @Test
    void testHostListIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));


        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getHostList(session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for hosts!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());

    }

    @Test
    void testInventoryHostOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"host1\", " +
                        "\"summary_fields\": {" +
                        "\"last_job\": {" +
                        "\"finished\": \"2023-05-09T12:34:56.789Z\", " +
                        "\"status\": \"success\"" +
                        "}}}]}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        int inventoryID = 1;
        List<Host> hostList = ansibleRep.getInventoryHost(session, inventoryID);

        //setter opp forventet variabler for hosten
        Host expectedHost = new Host(1, "host1", "09/05/2023", "success");

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, hostList.size());
        assertEquals(expectedHost.getId(), hostList.get(0).getId());
        assertEquals(expectedHost.getName(), hostList.get(0).getName());
        assertEquals(expectedHost.getDate(), hostList.get(0).getDate());
        assertEquals(expectedHost.getStatus(), hostList.get(0).getStatus());
    }

    @Test
    void testInventoryHostIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));

        int inventoryID = 1;
        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getInventoryHost(session, inventoryID);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for hosts i inventory!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());

    }

    @Test
    void testInventoryOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"host1\", " +
                        "\"summary_fields\": {" +
                        "\"last_job\": {" +
                        "\"finished\": \"2023-05-09T12:34:56.789Z\", " +
                        "\"status\": \"success\"" +
                        "}}}]}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        List<Inventory> inventoryList = ansibleRep.getInventory(session);

        //setter opp forventet variabler for hosten
        Inventory expectedInventory = new Inventory(1, "host1");

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, inventoryList.size());
        assertEquals(expectedInventory.getId(), inventoryList.get(0).getId());
        assertEquals(expectedInventory.getName(), inventoryList.get(0).getName());
    }

    @Test
    void testInventoryIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getInventory(session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for inventories!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());

    }

    @Test
    void testAuthTokenOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        StatusLine status = mock(StatusLine.class);

        String username = "test";
        String password = "test";


        // setter opp mock objektene til å returnere forventede verdiene
        when(status.getStatusCode()).thenReturn(201);
        when(response.getStatusLine()).thenReturn(status);
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"token\": \"abcd1234\"}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        String authToken = ansibleRep.getAuthToken(username, password);

        //setter opp forventet variabler for hosten
        String expectedAuthToken = "abcd1234";

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(expectedAuthToken, authToken);
    }

    @Test
    void testAuthTokenFeilStatusCode() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);
        StatusLine status = mock(StatusLine.class);

        String username = "test";
        String password = "test";


        // setter opp mock objektene til å returnere forventede verdiene
        when(status.getStatusCode()).thenReturn(300);
        when(response.getStatusLine()).thenReturn(status);
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"token\": \"abcd1234\"}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ansibleRep.getAuthToken(username, password);
        });

        //setter opp forventet variabler for hosten
        String expectedMessage = "Kunne ikke finne auth-token!!";

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(expectedMessage, exception.getMessage());
        assertEquals(RuntimeException.class, exception.getClass());
    }

    @Test
    void testConfigureOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"detail\": \"Success\"}").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        hosts.add("Host1");
        Patch patch = new Patch(1, hosts);
        String expectedResponse = "{\"detail\": \"Success\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ansibleRep.configurate(patch, session);

        System.setOut(null);

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals(expectedResponse, outContent.toString().trim());

    }


    @Test
    void testConfigureIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        hosts.add("Host1");
        Patch patch = new Patch(1, hosts);

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.configurate(patch, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Patch response er tom!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testConfigurePlaybookNotFound() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"detail\":\"Not found.\"}").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        hosts.add("Host1");
        Patch patch = new Patch(1, hosts);

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.configurate(patch, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Fant ikke playbook!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testConfigureInnPatcIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"detail\":\"Not found.\"}").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        Patch patch = new Patch(1, hosts);

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.configurate(patch, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Dataen som sendes til server er TOM!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }


    @Test
    void testHistorikkOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"host1\", " +
                        "\"status\": \"complete\", " +
                        "\"inventory\": \"InventoryTest\", " +
                        "\"started\": \"2023-05-10T14:30:00.000Z\", " +
                        "\"finished\": \"2023-05-10T14:30:00.000Z\"" +
                        "}]}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        List<Historikk> historikkList = ansibleRep.getHistorikk(session);

        //setter opp forventet variabler for hosten
        Historikk expectedHistorikk = new Historikk(1, "host1", "complete", "InventoryTest", "10/05/2023", "10/05/2023");

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, historikkList.size());
        assertEquals(expectedHistorikk.getId(), historikkList.get(0).getId());
        assertEquals(expectedHistorikk.getNavn(), historikkList.get(0).getNavn());
        assertEquals(expectedHistorikk.getStatus(), historikkList.get(0).getStatus());
        assertEquals(expectedHistorikk.getInventory(), historikkList.get(0).getInventory());
        assertEquals(expectedHistorikk.getStartTime(), historikkList.get(0).getStartTime());
        assertEquals(expectedHistorikk.getFinnishTime(), historikkList.get(0).getFinnishTime());
    }

    @Test
    void testHistorikkListIsNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));


        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getHistorikk(session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for historikk!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());

    }

    @Test
    void testSchedulePatchOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"detail\": \"Success\"}").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        hosts.add("Host1");
        Schedule schedule = new Schedule(1, "34224", "1", "test", hosts);
        String expectedResponse = "{\"detail\": \"Success\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ansibleRep.schedulePatch(schedule, session);

        System.setOut(null);

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals(expectedResponse, outContent.toString().trim());
    }


    @Test
    void testSchedulePatchReponseErNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        hosts.add("host");
        Schedule schedule = new Schedule(1, "22334455", "1", "test", hosts);

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.schedulePatch(schedule, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen response for schedulePatch!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testSchedulePatchInndataErFeil() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpPost.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));
        List<String> hosts = new ArrayList<>();
        Schedule schedule = new Schedule(1, "22334455", "1", "test", hosts);

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.schedulePatch(schedule, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Dataen som sendes til server er TOM!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testDeleteScheduleOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpDelete.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"detail\": \"Success\"}").getBytes()
        ));

        String expectedResponse = "{\"detail\": \"Success\"}";

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ansibleRep.deleteSchedule(1, session);

        System.setOut(null);

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals(expectedResponse, outContent.toString().trim());
    }

    @Test
    void testDeleteScheduleResponseErNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpDelete.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.deleteSchedule(1, session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Response er tom!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testGetScheduleOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"Patch\", " +
                        "\"rrule\": \"testRR\", " +
                        "\"unified_job_template\": \"20\", " +
                        "\"extra_data\": {" +
                        "\"host_name\": \"Host1\"" +
                        "}}]}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        List<Schedule> scheduleList = ansibleRep.getSchedule(session);
        List<String> hosts = new ArrayList<>();
        hosts.add("Host1");

        //setter opp forventet variabler for hosten
        Schedule expectedSchedule = new Schedule(1, "Patch", "testRR", "20", hosts);

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, scheduleList.size());
        assertEquals(expectedSchedule.getId(), scheduleList.get(0).getId());
        assertEquals(expectedSchedule.getNavn(), scheduleList.get(0).getNavn());
        assertEquals(expectedSchedule.getHosts(), scheduleList.get(0).getHosts());
        assertEquals(expectedSchedule.getRrule(), scheduleList.get(0).getRrule());
        assertEquals(expectedSchedule.getPlaybookId(), scheduleList.get(0).getPlaybookId());
    }

    @Test
    void testGetScheduleResponseErNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getSchedule(session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for schedules!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testGetPlaybookOK() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("{\"results\": " +
                        "[{\"id\": 1, " +
                        "\"name\": \"Patch\"" +
                        "}]}").getBytes()
        ));

        //kaller metoden for å bli testa på og lagre resultatene
        List<Playbook> playbookList = ansibleRep.getPlaybook(session);

        //setter opp forventet variabler for hosten
        Playbook expectedPlaybook = new Playbook(1, "Patch");

        //tester ut ved å sammenligne forventet host og returnete host
        assertEquals(1, playbookList.size());
        assertEquals(expectedPlaybook.getId(), playbookList.get(0).getId());
        assertEquals(expectedPlaybook.getNavn(), playbookList.get(0).getNavn());
    }

    @Test
    void testGetPlaybookResponseErNull() throws Exception {
        // Lager mock objekter
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        HttpEntity entity = mock(HttpEntity.class);

        // setter opp mock objektene til å returnere forventede verdiene
        when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(response);
        when(response.getEntity()).thenReturn(entity);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(
                ("").getBytes()
        ));

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.getPlaybook(session);
        });

        //tester ut ved å sammenligne forventet melding og faktisk melding
        assertEquals("Ingen resultater for playbooks!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
    }

    @Test
    void testDateFormatterOK() {
        String innDato = "2023-05-10T14:30:00.000Z";
        String expectedFormat = "10/05/2023";

        String actualFormat = ansibleRep.dateFormatter(innDato);

        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    void testDateFormatterInnDatoIsNull() {
        //2023-05-10T14:30:00.000Z   10/05/2023
        String innDato = null;

        //Kaller metoden å lagrer faktisk melding tilbake
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ansibleRep.dateFormatter(innDato);
        });

        assertEquals("Dato er tom!!", exception.getMessage());
        assertEquals(NullPointerException.class, exception.getClass());
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
