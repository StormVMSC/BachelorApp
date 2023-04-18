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

    @PostMapping("/patching")
    public void patchServer(@RequestBody Patch patch) throws NoSuchAlgorithmException, KeyManagementException {
        System.out.println("JAAAAA");
        System.out.print("Hosts; " + patch.getHosts() + "JobId; " + patch.getJobId());
        System.out.print(patch);

        AnsibleAPIRepository.patchRestCall(patch);
    }

}
