package com.example.bachelorapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class KundeController {
    public final List<Kunde> alleKunder = new ArrayList<>();

    @PostMapping("/lagre")
    public void lagreKunde(Kunde innKunde){
        alleKunder.add(innKunde);
    }
    @GetMapping("/hentAlle")
    public List<Kunde> hentAlle(){
        return alleKunder;
    }

    @GetMapping("/slettAlle")
    public void slettAlle(){
        alleKunder.clear();
    }
}
