package com.example.bachelorapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
public class AnsibleAPIController {
    @Autowired
    private AnsibleAPIRepository repo;
    @GetMapping("/GetHostList")
    public List<Host> gethosts() throws Exception {
        return repo.getHostList();
    }

    @GetMapping("/GetInventory")
    public List<Inventory> getInventories() throws IOException{
        return repo.getInventory();
    }

    @PostMapping("/PatchFlere")
    public void patchSomeCall() throws IOException {
        repo.patchSomeCall();
    }

    @GetMapping("/GetHistorikk")
    public List<Historikk> getHistorikk() throws IOException {
        return repo.getHistorikk();
    }

    @PostMapping("/schedulePatch")
    public void schedulePatch() throws IOException {
        repo.schedulePatch();
    }

}
