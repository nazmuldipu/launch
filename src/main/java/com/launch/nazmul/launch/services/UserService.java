package com.launch.nazmul.launch.services;

import com.launch.nazmul.launch.entities.User;
import com.launch.nazmul.launch.exceptions.exists.UserAlreadyExistsException;
import com.launch.nazmul.launch.exceptions.invalid.UserInvalidException;
import com.launch.nazmul.launch.exceptions.notfound.UserNotFoundException;
import com.launch.nazmul.launch.exceptions.nullpointer.NullPasswordException;
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

    boolean exists(User user);

}