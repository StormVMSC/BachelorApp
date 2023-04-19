package com.example.bachelorapp;

import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class KundeRepository {

    @Autowired
    private JdbcTemplate db;

    public boolean registrer(String username, String password) {
        // sjekker om brukeren eksisterer allerede

        String sql = "SELECT * FROM Kunde WHERE username = ?";
        Kunde kunde = db.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(Kunde.class));
        if(kunde != null){
            return false;
        }
        String hash = krypterPassord(password);
        sql = "INSERT INTO Kunde (username,adresse) VALUES(?,?)";
        int check = db.update(sql, username, hash);
        if(check == 1){
            return true;
        }else{
         return false;
        }
    }

    public List<Kunde> hentAlleKunder() {
        String sql = "SELECT * FROM Kunde";
        List<Kunde> alleKunder = db.query(sql,new BeanPropertyRowMapper(Kunde.class));
        return alleKunder;
    }
    public void slettAlleKunder () {
        String sql = "DELETE from KUNDE";
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

    public boolean login(String username, String passord, HttpSession session ){
        String sql = "SELECT * FROM bruker WHERE username = ?";
        Kunde kunde = db.queryForObject(sql, new Object[]{username}, new BeanPropertyRowMapper<>(Kunde.class));

        if(kunde != null && sjekkPassord(passord, kunde.getPassord())){
            String sessionId = UUID.randomUUID().toString();
            kunde.setSessionId(sessionId);
            updateSessionId(kunde);
            session.setAttribute("kunde", kunde);
            return true;
        }
        return false;
    }

    public void logout(HttpSession session){
        Kunde kunde = (Kunde) session.getAttribute("user");
        if(kunde != null){
            kunde.setSessionId(null);
            updateSessionId(kunde);
            session.invalidate();
        }
    }

    private void updateSessionId(Kunde kunde) {
        String sql =  "UPDATE users SET session_id = ? WHERE id = ?";
        db.update(sql, kunde.getSessionId(), kunde.getId());
    }

    public boolean isLoggedIn(HttpSession session){
        return session != null && session.getAttribute("user") != null;
    }
}
