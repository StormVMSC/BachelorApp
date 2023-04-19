package com.example.bachelorapp;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class KundeController {

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
    public void logout(HttpSession session){
        rep.logout(session);
    }

    @GetMapping("/isLoggedIn")
    public boolean isLoggedIn(HttpSession session){
        return rep.isLoggedIn(session);
    }





}
