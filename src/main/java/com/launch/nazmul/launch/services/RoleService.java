package com.launch.nazmul.launch.services;


import com.launch.nazmul.launch.entities.Role;
import com.launch.nazmul.launch.exceptions.forbidden.ForbiddenException;
import com.launch.nazmul.launch.exceptions.notfound.UserNotFoundException;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    Role findByRole(String role);

    Role findRole(Role.ERole role);

    List<Role> findByUser(Long userId) throws ForbiddenException, UserNotFoundException;
}