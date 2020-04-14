package ru.kpfu.itis.meow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meow")
public class MeowController {

    @GetMapping
    public String getHelloMeow() {
        return "Meow, fren!";
    }
}
