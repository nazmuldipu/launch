package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User save(User user) throws UserAlreadyExistsException, UserInvalidException, NullPasswordException;

    User getOne(Long id) throws UserNotFoundException;

    Page<User> findAll(int page);

    User findByUsername(String username) throws UserNotFoundException;

    User findByPhoneNumber(String phoneNumber) throws UserNotFoundException;

    User findByEmail(String email) throws UserNotFoundException;

    User findByUsernameOrPhone(String usernameOrPhone) throws UserNotFoundException;

    Page<User> searchUser(String query, int page, int size);

    Page<User> findByRole(String role, int page);

    List<User> findByRole(String role);

    List<User> getUserListByShipId(Long shipId);

    boolean exists(User user);

    User setRoles(Long id, String[] roles) throws UserNotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    User toggleUser(Long id, boolean enabled) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException, UserNotFoundException;

    User changePassword(Long userId, String password) throws ForbiddenException, NullPasswordException;

    // *******************************ADMIN MODULES ****************************************
    User createAdminAgent(User user) throws ForbiddenException, NullPasswordException;

    Page<User> getAdminAgents(int page);

    User removeAdminAgent(Long userId) throws ForbiddenException, UserNotFoundException;

    User changeUserPasswordByAdmin(Long userId, String password) throws UserNotFoundException, NullPasswordException;

    User assignShipAndRole(Long userId, Long shipId, Role.ERole role) throws NotFoundException;

    // **************************** Service admin modules **********************************
    User addServiceAdminUser(User user) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    User createServiceAdminAgent(User user) throws ForbiddenException, NullPasswordException;

    Page<User> getServiceAdminAgents(Long shipId, int page) throws ForbiddenException;

    User removeServiceAdminAgent(Long userId) throws ForbiddenException, UserNotFoundException;

}