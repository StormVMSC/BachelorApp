package com.example.bachelorapp;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class KundeTest {

    @Mock
    HttpSession session;
    @Mock
    private AnsibleAPIRepository ansibleRepo;
    @Mock
    private JdbcTemplate db;

    @Mock
    Kunde kunde;

    @InjectMocks
    private KundeRepository kundeRepo;

    @InjectMocks
    private KundeController kundeController;

    @Test
    void contextLoads() {
    }
    @Test
    void testLogin() throws IOException{
        // Arrange
        String username = "testuser";
        String password = "testpassword";
        String expectedAuthToken = "testauthtoken";
        String expectedSessionId = "testsessionid";

        when(db.queryForObject(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class))).thenReturn(kunde);
        when(kunde.getPassord()).thenReturn(krypterPassord("testpassword"));
        when(ansibleRepo.getAuthToken(eq(username), eq(password))).thenReturn(expectedAuthToken);
        when(kunde.getSessionId()).thenReturn(expectedSessionId);

        // Act
        boolean result = kundeRepo.login(username, password, session);

        // Assert
        assertTrue(result);
        verify(session).setAttribute("auth", expectedAuthToken);
        verify(session).setAttribute("kunde", kunde);
    }

    @Test
    void testLogout(){
        HttpSession session = mock(HttpSession.class);
        Kunde kunde = mock(Kunde.class);
        when(session.getAttribute("kunde")).thenReturn(kunde);
        boolean result = kundeRepo.logout(session);
        verify(kunde).setSessionId(null);
        verify(session).invalidate();
        assertTrue(result);
    }

    @Test
    void testRegistrer(){
        String username = "testuser";
        String password = "testpassword";
        String hashedPassword = "hashedpassword";
        Kunde expectedKunde = new Kunde();
        expectedKunde.setUsername(username);
        expectedKunde.setPassord(password);

        when(db.queryForObject(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class))).thenReturn(null);
        when(db.update(anyString(), anyString(), anyString())).thenReturn(1);

        boolean result = kundeRepo.registrer(username, password);

        verify(db).queryForObject(anyString(), any(Object[].class), any(BeanPropertyRowMapper.class));
        verify(db).update(anyString(), anyString(), anyString());
        assertTrue(result);
    }


    private String krypterPassord(String passord){
        if (passord == null || passord.isEmpty()) {
            throw new NullPointerException("Passord er ugyldig!!");
        }
        String kryptertPassord = BCrypt.hashpw(passord, BCrypt.gensalt(15));
        return kryptertPassord;
    }

}
