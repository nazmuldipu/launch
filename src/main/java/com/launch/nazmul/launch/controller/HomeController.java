package com.launch.nazmul.launch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.launch.nazmul.launch.entities.User;
import com.launch.nazmul.launch.exceptions.exists.UserAlreadyExistsException;
import com.launch.nazmul.launch.exceptions.invalid.InvalidException;
import com.launch.nazmul.launch.exceptions.nullpointer.NullPasswordException;
import com.launch.nazmul.launch.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Launch.com", HttpStatus.OK);
    }



    @PostMapping("/register")
    private ResponseEntity<User> register(@ModelAttribute User user, BindingResult bindingResult,
                                          @RequestParam(value = "sendPassword", defaultValue = "false") Boolean sendPassword
    ) throws UserAlreadyExistsException, InvalidException, NullPasswordException, JsonProcessingException {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().build();
        user = this.userService.save(user);
        return ResponseEntity.ok(user);
    }
}
