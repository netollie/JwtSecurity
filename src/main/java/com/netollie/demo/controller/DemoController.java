package com.netollie.demo.controller;

import com.netollie.demo.annotation.LoginRequired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @LoginRequired
    @GetMapping("/security")
    public String security() {
        return "this is security page";
    }

    @GetMapping("/open")
    public String open() {
        return "this is open page";
    }
}
