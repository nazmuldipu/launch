package com.ship.nazmul.ship.controller.api.users;

import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/users")
public class UserAdminController {
    private final UserService userService;

    @Autowired
    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity all(@RequestParam(value = "page", defaultValue = "0") Integer page,
                              @RequestParam(value = "role", required = false) String role) {
        Page<User> userPage;
        if (role == null)
            userPage = this.userService.findAll(page);
        else
            userPage = this.userService.findByRole(role, page);

        return ResponseEntity.ok(userPage);
    }

    @GetMapping("/getShipAdminList")
    public ResponseEntity getAllShipAdmin(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.userService.findByRole(Role.ERole.ROLE_SERVICE_ADMIN.getValue(),page));
    }

    @PostMapping("")
    private ResponseEntity<User> createAdminUser(@RequestBody User user) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException, UserNotFoundException {
        return ResponseEntity.ok(this.userService.save(user));
    }

    @PostMapping("/{id}/access/toggle")
    public ResponseEntity disableUser(@PathVariable("id") Long id,
                                      @RequestParam("enabled") boolean enabled) throws UserNotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        return ResponseEntity.ok(this.userService.toggleUser(id, enabled));
    }

    @GetMapping("/{userId}")
    private ResponseEntity<User> getAdminUser(@PathVariable("userId") Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(this.userService.getOne(userId));
    }

    //Search user by phone number
    @PutMapping("/searchUser")
    private ResponseEntity<User> findUserByPhoneNumber(@RequestParam("phone") String phone) throws UserNotFoundException {
        return ResponseEntity.ok(this.userService.findByUsernameOrPhone(phone));
    }

    // Create Agent for Service admin
    @PostMapping("/createAgent")
    private ResponseEntity<User> creatAdminAgent(@RequestBody User user) throws ForbiddenException, NullPasswordException {
        return ResponseEntity.ok(this.userService.createAdminAgent(user));
    }

    @PutMapping("/{id}/changeRole")
    private ResponseEntity changeRole(@PathVariable("id") Long id,
                                      @RequestParam("roles") String[] roles) throws UserNotFoundException, UserInvalidException, UserAlreadyExistsException, NullPasswordException {
        User user = this.userService.setRoles(id, roles);
        return ResponseEntity.ok(user);
    }

    //Get Service Admin Agent Page
    @GetMapping("/myAgents")
    private ResponseEntity<Page<User>> getAdminAgentPage(@RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        return ResponseEntity.ok(this.userService.getAdminAgents(page));
    }

    @GetMapping("/searchByShipId/{shipId}")
    private ResponseEntity<List<User>> getUserListByShipId(@PathVariable("shipId") Long shipId) {
        return ResponseEntity.ok(this.userService.getUserListByShipId(shipId));
    }

    @GetMapping("/getUserShipList/{userId}")
    private ResponseEntity<Set<Ship>> getUserShipList(@PathVariable("userId") Long userId) throws UserNotFoundException {
        return ResponseEntity.ok(this.userService.getUserShipList(userId));
    }

    //Remove Service admin Agent
    @PutMapping("/removeAgent/{userId}")
    private ResponseEntity<User> removeAdminAgent(@PathVariable("userId") Long userId) throws ForbiddenException, UserNotFoundException {
        return ResponseEntity.ok(this.userService.removeAdminAgent(userId));
    }

    // Change User password
    @PutMapping("/changeUserPassword/{userId}")
    private ResponseEntity<User> changeUserPassword(@PathVariable("userId") Long userId, @RequestParam("password") String password) throws UserNotFoundException, NullPasswordException {
        return ResponseEntity.ok(this.userService.changeUserPasswordByAdmin(userId, password));
    }

    @PatchMapping("/assignShipAgent/{userId}")
    public ResponseEntity assignHotel(@PathVariable("userId") Long userId,
                                      @RequestParam("shipId") Long shipId) throws NotFoundException {
//        User user = this.userService.assignShipAndRole(userId, shipId, role);
        User user = this.userService.assignShipAgent(userId, shipId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/assignShipAdmin/{userId}")
    public ResponseEntity assignShipAdmin(@PathVariable("userId") Long userId,
                                          @RequestParam("shipId") Long shipId) throws InvalidException, NotFoundException, ForbiddenException {
        User user = this.userService.assignShipAdmin(userId, shipId);
        return ResponseEntity.ok(user);
    }
}
