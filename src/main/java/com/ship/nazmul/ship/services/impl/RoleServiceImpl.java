package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.repositories.RoleRepository;
import com.ship.nazmul.ship.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    //    private UserRepository userRepo;
    private final RoleRepository roleRepo;

    @Autowired
    public RoleServiceImpl( RoleRepository roleRepo) {
//        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public Role findRole(Role.ERole eRole) {
        Role role = this.findByRole(eRole.toString());
        if (role==null) {
            role = new Role(eRole);
            role = this.save(role);
        }
        return role;
    }

    @Override
    public Role findByRole(String role) {
        if (role==null) throw new IllegalArgumentException("Role can not be null!");
        return this.roleRepo.findByRole(role);
    }

    @Override
    public Role save(Role role) {
        if (role==null) throw new IllegalArgumentException("Role can not be null!");
        return this.roleRepo.save(role);
    }

    @Override
    public List<Role> findByUser(Long userId) throws ForbiddenException, UserNotFoundException {
//        User user = this.userRepo.findOne(userId);
//        if (user == null) throw new UserNotFoundException("Could not find user with id " + userId);
//        return user.getRoles();
        return  null;
    }
}