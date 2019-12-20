package com.ship.nazmul.ship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Ship.com", HttpStatus.OK);
    }



    @PostMapping("/register")
    private ResponseEntity<User> register(@ModelAttribute User user, BindingResult bindingResult,
                                          @RequestParam(value = "sendPassword", defaultValue = "false") Boolean sendPassword
    ) throws UserAlreadyExistsException, InvalidException, NullPasswordException, JsonProcessingException, UserNotFoundException {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().build();
        user = this.userService.save(user);
        return ResponseEntity.ok(user);
    }
}
