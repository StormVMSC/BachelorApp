package com.example.bachelorapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class AnsibleAPIController {

    @PostMapping("/job")
    public void job(String username, String password, int jobtemplate) throws IOException {
        AnsibleAPIRepository.job(username, password,jobtemplate);
    }
}
