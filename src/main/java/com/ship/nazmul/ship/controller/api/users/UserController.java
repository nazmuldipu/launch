package com.ship.nazmul.ship.controller.api.users;

import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Change User password
    @PutMapping("/changePassword/{userId}")
    private ResponseEntity<User> changeUserPassword(@PathVariable("userId")Long userId, @RequestParam("password")String password) throws NullPasswordException, ForbiddenException, UserAlreadyExistsException, UserInvalidException, UserNotFoundException {
        return ResponseEntity.ok(this.userService.changePassword(userId, password));
    }

//    /*Get user permissions
//    * @Output permissions
//    */
//    @GetMapping("/premissions")
//    private ResponseEntity<>
}
