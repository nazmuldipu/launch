package com.ship.nazmul.ship.services;


import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    Role findByRole(String role);

    Role findRole(Role.ERole role);

    List<Role> findByUser(Long userId) throws ForbiddenException, UserNotFoundException;
}