package com.example.inventorysystembackend.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Inventory System API Running";
    }
}
