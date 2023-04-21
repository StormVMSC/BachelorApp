package com.example.bachelorapp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class AnsibleAPIController {
    @Autowired
    private AnsibleAPIRepository repo;
    @GetMapping("/GetHostList")
    public List<Host> gethosts(HttpSession session) throws Exception {
        return repo.getHostList(session);
    }

    @GetMapping("/GetInventory")
    public List<Inventory> getInventories(HttpSession session) throws IOException{
        return repo.getInventory(session);
    }

    @PostMapping("/PatchFlere")
    public void patchSomeCall(Patch patch, HttpSession session) throws IOException {
        repo.patchSomeCall(patch, session);
    }

    @GetMapping("/GetHistorikk")
    public List<Historikk> getHistorikk(HttpSession session) throws IOException {
        return repo.getHistorikk(session);
    }

    @PostMapping("/schedulePatch")
    public void schedulePatch(@RequestBody Schedule schedule , HttpSession session) throws IOException {
        repo.schedulePatch(schedule, session);
    }

    @PostMapping("/deleteSchedule")
    public void deleteSchedule(Integer id, HttpSession session) throws IOException {
        repo.deleteSchedule(id, session);
    }

    @GetMapping("/GetSchedules")
    public List<Schedule> getSchedules(HttpSession session) throws IOException {
        return repo.getSchedule(session);
    }

}
