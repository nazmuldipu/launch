package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.PasswordUtil;
import com.ship.nazmul.ship.entities.Role;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.repositories.UserRepository;
import com.ship.nazmul.ship.services.RoleService;
import com.ship.nazmul.ship.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleService roleService;

    @Value("${baseUrlApi}")
    private String baseUrlApi;
    @Value(("${admin.phone1}"))
    private String adminPhone1;
    @Value(("${admin.phone2}"))
    private String adminPhone2;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleService roleService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
    }

    @Override
    public User save(User user) throws UserAlreadyExistsException, UserInvalidException, NullPasswordException {
        if (!this.isValid(user)) throw new UserInvalidException("User invalid");

        // check if user already exists
        if (user.getId() == null && this.exists(user))
            throw new UserAlreadyExistsException("User already exists with this email or username");
        if (user.getPhoneNumber() == null)
            throw new UserInvalidException("User Phone number can not be null");
        if (user.getPassword() == null || user.getPassword().length() < 6)
            throw new UserInvalidException("Password cannot be null or length must be at least 6 or more!");

        // set Roles
        user.grantRole(this.roleService.findRole(Role.ERole.ROLE_USER));

        // Execute only when user is being registered
        if (user.getId() == null) {
            // Encrypt passwprd
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
            if (user.getPhoneNumber().equals(this.adminPhone1) || user.getPhoneNumber().equals(this.adminPhone2)) {
                user.grantRole(this.roleService.findRole(Role.ERole.ROLE_ADMIN));
            }

            // flood control
//            String ip = NetworkUtil.getClientIP();
//            if (this.registrationAttemptService.isBlocked(ip))
//                throw new UserInvalidException("Maximum limit exceed!");
//            this.registrationAttemptService.registrationSuccess(ip);
        }

        return this.userRepo.save(user);
    }
    private boolean isValid(User user) {
        return user != null && user.getPassword() != null;
    }

    @Override
    public User getOne(Long id) throws UserNotFoundException {
        if (id == null) throw new UserNotFoundException("User id can not be null!");
        if (!this.userRepo.exists(id)) {
            throw new UserNotFoundException("User not found");
        }
        return this.userRepo.findOne(id);
    }

    @Override
    public Page<User> findAll(int page) {
        if (page < 0) page = 0;
        return this.userRepo.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException {
        if (username == null) throw new UserNotFoundException("Username can not be null!");
        return this.userRepo.findByUsername(username);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) throws UserNotFoundException {
        if (phoneNumber == null) throw new UserNotFoundException("Phone number can not be null!");
        return this.userRepo.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        if (email == null) throw new UserNotFoundException("Email can not be null!");
        return this.userRepo.findByPhoneNumber(email);
    }

    @Override
    public User findByUsernameOrPhone(String usernameOrPhone) throws UserNotFoundException {
        User user = this.userRepo.findByUsername(usernameOrPhone);
        if (user == null)
            user = this.userRepo.findByPhoneNumber(usernameOrPhone);
        if (user == null)
            throw new UserNotFoundException("Could not find user with username or email " + usernameOrPhone);
        return user;
    }

    @Override
    public Page<User> searchUser(String query, int page, int size) {
        return this.userRepo.searchByNameOrUsername(query, PageAttr.getPageRequest(page, size));
    }

    @Override
    public boolean exists(User user) {
        if (user == null) throw new IllegalArgumentException("user can't be null");
        boolean email = false;

        if (user.getEmail() != null && user.getEmail().length() > 3) {
            email = this.userRepo.findByEmail(user.getEmail()) != null;
        }
        return this.userRepo.findByUsername(user.getUsername()) != null
                || this.userRepo.findByPhoneNumber(user.getPhoneNumber()) != null || email;
    }

    @Override
    public Page<User> findByRole(String role, int page) {
        return this.userRepo.findByRolesName(role, PageAttr.getPageRequest(page));
    }

    @Override
    public List<User> findByRole(String role) {
        return this.userRepo.findByRolesName(role);
    }


}
