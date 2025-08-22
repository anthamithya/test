package com.welcomeletterservice.web.rest;

import com.welcomeletterservice.service.WelcomeLetterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WelcomeLetterResource {
    private final WelcomeLetterService welcomeLetterService;

    public WelcomeLetterResource(WelcomeLetterService welcomeLetterService) {
        this.welcomeLetterService = welcomeLetterService;
    }

    @GetMapping("/letter-generation")
    public void letterGeneration() {
        welcomeLetterService.letterGeneration();
    }

}
