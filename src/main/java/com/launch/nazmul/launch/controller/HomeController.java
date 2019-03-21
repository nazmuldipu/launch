package com.launch.nazmul.launch.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Launch.com.bd", HttpStatus.OK);
    }

}
