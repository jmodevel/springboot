package com.jmo.devel.zuul.testing.ping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PongController {

    @GetMapping("/")
    public String index() {
        return "Pong";
    }

}
