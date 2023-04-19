package com.example.bachelorapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KundeController {

    @PostMapping("/registrer")
    public boolean registrer(Kunde kunde){
        return true;
    }

    @GetMapping("/login")
    public boolean login(Kunde kunde){

        return true;
    }
}
