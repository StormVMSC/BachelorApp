package com.example.bachelorapp;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Repository
public class KundeRepository{

    @Autowired
    private AnsibleAPIRepository Ansible;
    @Autowired
    private JdbcTemplate db;

    public boolean registrer(String username, String password) {
        // sjekker om brukeren eksisterer allerede

        String sql = "SELECT * FROM Kunde WHERE username = ?";
        Kunde kunde = null;
        try{
            kunde = db.queryForObject(sql, new Object[]{"'" + username + "'"} , new BeanPropertyRowMapper<>(Kunde.class));
        }catch(EmptyResultDataAccessException e){
        }
        if(kunde == null){
            String hash = krypterPassord(password);
            sql = "INSERT INTO kunde (username, passord) VALUES(?,?)";
            int check = db.update(sql, username, hash);
            return check == 1;
        } else {
            return false;
        }
    }

    public List<Kunde> hentAlleKunder() {
        String sql = "SELECT * FROM Kunde";
        List<Kunde> alleKunder = db.query(sql,new BeanPropertyRowMapper(Kunde.class));
        return alleKunder;
    }
    public void slettAlleKunder () {
        String sql = "DELETE from Kunde";
        db.update(sql);
    }

    private String krypterPassord(String passord){
        String kryptertPassord = BCrypt.hashpw(passord, BCrypt.gensalt(15));
        return kryptertPassord;
    }
    private boolean sjekkPassord(String passord, String hashPassord){
        boolean ok = BCrypt.checkpw(passord, hashPassord);
        return ok;
    }

    public boolean login(String username, String passord, HttpSession session ) throws IOException {
        String sql = "SELECT * FROM Kunde WHERE username = ?";
        Kunde kunde = db.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(Kunde.class));
        if(kunde != null && sjekkPassord(passord, kunde.getPassord())){

            //String auth = Ansible.getAuthToken(username, passord);
            session.setAttribute("auth", "yes");
            String sessionId = UUID.randomUUID().toString();
            kunde.setSessionId(sessionId);
            updateSessionId(kunde);
            session.setAttribute("kunde", kunde);
            return true;
        }
        return false;
    }

    public boolean logout(HttpSession session){
        Kunde kunde = (Kunde) session.getAttribute("kunde");
        if(kunde != null){
            kunde.setSessionId(null);
            updateSessionId(kunde);
            session.invalidate();
            return true;
        }
        return false;
    }

    private void updateSessionId(Kunde kunde) {
        String sql =  "UPDATE Kunde SET session_id = ? WHERE id = ?";
        db.update(sql, kunde.getSessionId(), kunde.getId());
    }

    public boolean isLoggedIn(HttpSession session){
        return session != null && session.getAttribute("kunde") != null;
    }

    @PostConstruct
    private void brukerInsert(){
        registrer("admin", "redhat");
    }
}

