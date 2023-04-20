package com.example.bachelorapp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class KundeController {
    @Autowired
    KundeRepository rep;

    @PostMapping("/registrer")
    public boolean registrer(String username, String password){
        return rep.registrer(username, password);
    }

    @PostMapping("/login")
    public boolean login(String username, String password , HttpSession session){
        return rep.login(username, password, session);
    }

    @PostMapping("/logout")
    public boolean logout(HttpSession session){
        return rep.logout(session);
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(HttpSession session){
        return rep.isLoggedIn(session);
    }





}
