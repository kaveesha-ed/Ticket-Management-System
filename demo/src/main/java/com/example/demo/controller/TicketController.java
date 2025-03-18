package com.example.demo.controller;

import com.example.demo.model.Configuration;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/configuration")
    public String saveConfiguration(@RequestBody Configuration configuration) {
        ticketService.saveConfiguration(configuration);
        return "Configuration saved successfully!";
    }

    @GetMapping("/count")
    public int getTicketCount() {
        return ticketService.getTicketCount();
    }

    @PostMapping("/start")
    public String startOperation() {
        ticketService.startOperation();
        return "System started!";
    }

    @PostMapping("/stop")
    public String stopOperation() {
        ticketService.stopOperation();
        return "System stopped!";
    }

    @GetMapping("/logs")
    public List<String> getLogs() {
        return ticketService.getLogs();
    }
}
