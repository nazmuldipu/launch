package com.ship.nazmul.ship.controller.api.users;

import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/serviceAdmin/users")
public class UserServiceAdminController {
    private final UserService userService;

    @Autowired
    public UserServiceAdminController(UserService userService) {
        this.userService = userService;
    }

    // Create user while booking a room by service admin
    @PostMapping("")
    private ResponseEntity<User> createServiceAdminUser(@RequestBody User user) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        return ResponseEntity.ok(this.userService.addServiceAdminUser(user));
    }

    // Create Agent for Service admin
    @PostMapping("/createAgent")
    private ResponseEntity<User> creatServiceAdminAgent(@RequestBody User user) throws ForbiddenException, NullPasswordException {
        return ResponseEntity.ok(this.userService.createServiceAdminAgent(user));
    }

    //Get Service Admin Agent Page
    @GetMapping("/myAgents")
    private ResponseEntity<Page<User>> getServiceAdminAgentPage(@RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        return ResponseEntity.ok(this.userService.getServiceAdminAgents(page));
    }

    //Remove Service admin Agent
    @PutMapping("/removeAgent/{userId}")
    private ResponseEntity<User> removeServiceAdminAgent(@PathVariable("userId")Long userId) throws ForbiddenException, UserNotFoundException {
        return ResponseEntity.ok(this.userService.removeServiceAdminAgent(userId));
    }

}
