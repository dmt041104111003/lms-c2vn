package com.cardano_lms.server.Controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello{

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @PostMapping("/hello")
    public String sayHelloPost(String str) {
        return str;
    }
}
