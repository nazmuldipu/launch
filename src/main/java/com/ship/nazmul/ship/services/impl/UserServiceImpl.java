package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.utils.PasswordUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
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
import com.ship.nazmul.ship.repositories.UserRepository;
import com.ship.nazmul.ship.services.RoleService;
import com.ship.nazmul.ship.services.ShipService;
import com.ship.nazmul.ship.services.UserService;
import com.ship.nazmul.ship.services.accountings.ShipAgentLedgerService;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final RoleService roleService;
    private final ShipService shipService;
    private final ShipAgentLedgerService shipAgentLedgerService;

    @Value("${baseUrlApi}")
    private String baseUrlApi;
    @Value(("${admin.phone1}"))
    private String adminPhone1;
    @Value(("${admin.phone2}"))
    private String adminPhone2;

    @Autowired
    public UserServiceImpl(UserRepository userRepo, RoleService roleService, ShipService shipService, @Lazy ShipAgentLedgerService shipAgentLedgerService) {
        this.userRepo = userRepo;
        this.roleService = roleService;
        this.shipService = shipService;
        this.shipAgentLedgerService = shipAgentLedgerService;
    }

    @Override
    public User save(User user) throws UserAlreadyExistsException, UserInvalidException, NullPasswordException, UserNotFoundException {
        if (!this.isValid(user)) throw new UserInvalidException("User invalid");

        // check if user already exists
        if (user.getId() == null && this.exists(user))
            throw new UserAlreadyExistsException("User already exists with this email or username");
        if (user.getPhoneNumber() == null)
            throw new UserInvalidException("User Phone number can not be null");
        if (user.getPassword() == null || user.getPassword().length() < 6)
            throw new UserInvalidException("Password cannot be null or length must be at least 6 or more!");

        // set Roles
        if (user.getRoles() == null || user.getRoles().size() == 0) {
            user.grantRole(this.roleService.findRole(Role.ERole.ROLE_USER));
        }

        // Execute only when user is being registered
        if (user.getId() == null) {
            // Encrypt passwprd
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
            if (user.getPhoneNumber().equals(this.adminPhone1) || user.getPhoneNumber().equals(this.adminPhone2)) {
                user.grantRole(this.roleService.findRole(Role.ERole.ROLE_ADMIN));
            }
        } else {
            User oldUser = this.getOne(user.getId());
            user.setShips(oldUser.getShips());
            user.setRoles(oldUser.getRoles());
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
        if (usernameOrPhone == null) throw new UserNotFoundException("Username or Phone number can not be null!");
        User user = this.userRepo.findByUsername(usernameOrPhone);
        if (user == null)
            user = this.userRepo.findByPhoneNumber(usernameOrPhone);
//        if (user == null)
//            throw new UserNotFoundException("Could not find user with username or email " + usernameOrPhone);
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
    public User setRoles(Long id, String[] roles) throws UserNotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        User user = this.getOne(id);
        boolean isAdmin = user.isAdmin(); // check if user admin
        user.getRoles().clear();
        if (isAdmin)  // set admin role explicitly after clearing roles
            return user;
//            user.getRoles().add(this.roleService.findRole(Role.ERole.ROLE_ADMIN));
        // add roles
        for (String roleName : roles) {
            Role role = this.roleService.findRole(Role.getERoleFromRoleName(roleName));
            if (role != null)
                user.grantRole(role);
        }
        return this.save(user);
    }

    @Override
    public User toggleUser(Long id, boolean enabled) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException, UserNotFoundException {
        User user = this.getOne(id);
        user.setEnabled(enabled);
        user = this.save(user);
        return user;
    }

    @Override
    public User changePassword(Long userId, String password) throws ForbiddenException, NullPasswordException, UserAlreadyExistsException, UserInvalidException, UserNotFoundException {
        User user = SecurityConfig.getCurrentUser();
        if (user == null || user.getId() != userId) throw new ForbiddenException("A slap on your face, idiot");
        user.setPassword(PasswordUtil.encryptPassword(password, PasswordUtil.EncType.BCRYPT_ENCODER, null));
        return this.save(user);
    }

    @Override
    public User createAdminAgent(User user) throws ForbiddenException, NullPasswordException {
        //Security check
        User adminUser = SecurityConfig.getCurrentUser();
        if (!adminUser.isAdmin()) throw new ForbiddenException("Access Denied");

        //If user exists and user doesn't belongs to my hotel then access denied
        User oldUser = this.userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (oldUser != null && !oldUser.isOnlyUser() && oldUser.getShips().size() > 0) {
            throw new ForbiddenException("Access denied, this user already agent for another account.");
        }

        if (oldUser != null) {
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.changeRole(this.roleService.findRole(Role.ERole.ROLE_AGENT));
            return this.userRepo.save(oldUser);
        } else {
            user.setUsername(user.getPhoneNumber());
            user.setPassword(user.getPhoneNumber().substring(user.getPhoneNumber().length() - 6));
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_AGENT));
            return this.userRepo.save(user);
        }
    }

    @Override
    public Page<User> getAdminAgents(int page) {
        return this.userRepo.findByRolesName(Role.ERole.ROLE_AGENT.getValue(), PageAttr.getPageRequest(page));
    }

    @Override
    public User removeAdminAgent(Long userId) throws ForbiddenException, UserNotFoundException {
        // Security check
        User adminUser = SecurityConfig.getCurrentUser();
        if (!adminUser.isAdmin()) throw new ForbiddenException("Access Denied");

        User user = this.getOne(userId);
        if (user.isOnlyUser()) {
            return user;
        } else if (user.hasRole(Role.ERole.ROLE_AGENT.toString())) {
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_USER));
            user.setShips(null);
            return this.userRepo.save(user);
        }
        return null;
    }

    @Override
    public User changeUserPasswordByAdmin(Long userId, String password) throws UserNotFoundException, NullPasswordException {
        User user = this.getOne(userId);
        user.setPassword(PasswordUtil.encryptPassword(password, PasswordUtil.EncType.BCRYPT_ENCODER, null));
        return this.userRepo.save(user);
    }

    @Override
    public User assignShipAndRole(Long userId, Long shipId, Role.ERole role) throws NotFoundException {
        User user = this.getOne(userId);
        Ship ship = this.shipService.getOne(shipId);
        user.getShips().add(ship);
        Role r = this.roleService.findRole(role);
        user.changeRole(r);
        user = this.userRepo.save(user);
        return user;
    }

    @Override
    public User assignShipAdmin(Long userId, Long shipId) throws NotFoundException, ForbiddenException, InvalidException {
        //Get provided User and Ship object
        User user = this.getOne(userId);
        Ship ship = this.shipService.getOne(shipId);

        //Remove all previous Ship admin
        List<User> currentShipUsers = this.getUserListByShipId(shipId);
        for (User csu : currentShipUsers) {
            if (csu.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString())) {
                csu.getShips().remove(ship);
                csu.changeRole(this.roleService.findRole(Role.ERole.ROLE_USER));
                this.userRepo.save(csu);
            }
        }

        //Add ship into user ShipList and save
        if (!user.getShips().contains(ship)) {
            user.getShips().add(ship);
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_SERVICE_ADMIN));
            user = this.userRepo.save(user);
        } else if (user.hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString())) {
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_SERVICE_ADMIN));
            user = this.userRepo.save(user);
        }

        //Set ship admin and save
        ship.setAdmin(user);
        this.shipService.save(ship);
        return user;
    }

    @Override
    public User assignShipAgent(Long userId, Long shipId) throws NotFoundException {
        //Get provided User and Ship object
        User user = this.getOne(userId);
        Ship ship = this.shipService.getOne(shipId);

        //Add ship into user ShipList and save
        if (!user.getShips().contains(ship)) {
            user.getShips().add(ship);
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_SERVICE_AGENT));
            user = this.userRepo.save(user);
        }
        return user;
    }

    @Override
    public User addServiceAdminUser(User user) throws UserAlreadyExistsException, NullPasswordException, UserInvalidException, UserNotFoundException {
        User oldUser = this.userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (oldUser != null) return oldUser;

        user.setUsername(user.getPhoneNumber());
        user.setPassword(user.getPhoneNumber().substring(user.getPhoneNumber().length() - 6));
        if (user.getEmail() != null && user.getEmail().length() < 3) {
            user.setEmail(null);
        }
        return this.save(user);
    }

    /*Create Service admin agent
    * @param user   user object to be add
    * @return user  agent object after creating agent
    * Setps: 1) Check if user with phone number exists
    *       2) if exist and has ship list then Access denied
    *       3) if exist but ship list size is zero then add as current user agent
    *       4) if not exits then create user as agent*/
    @Override
    public User createServiceAdminAgent(User user) throws ForbiddenException, NullPasswordException {
        //Security check
        User currentUser = SecurityConfig.getCurrentUser();
        Set<Ship> ships = currentUser.getShips();
        if (ships == null || !currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access Denied");

        //If user exists and user doesn't belongs to my hotel then access denied
        User oldUser = this.userRepo.findByPhoneNumber(user.getPhoneNumber());
        if (oldUser != null &&
                ((!oldUser.isOnlyUser() && oldUser.getShips().size() != 0 && !this.containsShipListFromAnotherList(ships, oldUser.getShips()) )
                        || (oldUser.hasRole(Role.ERole.ROLE_AGENT.toString()))))
            throw new ForbiddenException("Access denied");

        if (oldUser != null) {
            oldUser.setCommission(user.getCommission());
            oldUser.setName(user.getName());
            oldUser.setEmail(user.getEmail());
            oldUser.changeRole(this.roleService.findRole(Role.ERole.ROLE_SERVICE_AGENT));
            oldUser.getShips().clear();
            oldUser.getShips().addAll(ships);
            return this.userRepo.save(oldUser);
        } else {
            user.setUsername(user.getPhoneNumber());
            user.setPassword(user.getPhoneNumber().substring(user.getPhoneNumber().length() - 6));
            user.setPassword(PasswordUtil.encryptPassword(user.getPassword(), PasswordUtil.EncType.BCRYPT_ENCODER, null));
            user.changeRole(this.roleService.findRole(Role.ERole.ROLE_SERVICE_AGENT));
            user.getShips().clear();
            this.userRepo.save(user);
            user.getShips().addAll(ships);
            return this.userRepo.save(user);
        }
    }

    /*Find if any ship from a set of ship matches with another set of ship
    * @param adminShips     Set of ship admin ships
    * @param userShips      set of user ships
    * @return true  if any ship of user ship set contains in admin ship set
    * */
    private boolean containsShipListFromAnotherList(Set<Ship> adminShips, Set<Ship> userShips){
        Iterator<Ship> adminShipsItr = adminShips.iterator();
        Iterator<Ship> userShipsItr = userShips.iterator();
        while(adminShipsItr.hasNext()){
            Ship adminShip = adminShipsItr.next();
            while (userShipsItr.hasNext()){
                Ship userShip = userShipsItr.next();
                if(adminShip.getId().equals(userShip.getId())){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Page<User> getServiceAdminAgents(int page) throws ForbiddenException {
        Set<Ship> ships = SecurityConfig.getCurrentUser().getShips();
        if (ships == null || !SecurityConfig.getCurrentUser().hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");
        List<Long> shipsId = new ArrayList<>();
        for (Ship ship : ships) {
            shipsId.add(ship.getId());
        }

        return this.userRepo.findDistinctByShipsIdInAndRolesName(shipsId, Role.ERole.ROLE_SERVICE_AGENT.getValue(), PageAttr.getPageRequest(page));
    }

    /*Remove Service Admin Agent from ship
    * @param userId     id of user to be remove
    *
    * Steps: 1) Remove balance from serviceAgent account
    *       2)Remove all ship from service agent account*/
    @Override
    public User removeServiceAdminAgent(Long userId) throws ForbiddenException, UserNotFoundException {
        // Security check
        User serviceUser = SecurityConfig.getCurrentUser();
        if (!serviceUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        Set<Ship> ships = serviceUser.getShips();
        if (ships == null) throw new ForbiddenException("Ships cannot be null");

        User user = this.getOne(userId);
        if (user.isOnlyUser()) {
            return user;
        } else if (user.hasRole(Role.ERole.ROLE_SERVICE_AGENT.toString()) && user.getShips() != null) {
            int agentBalance = this.shipAgentLedgerService.getServiceAgentBalance(userId);
            if (agentBalance > 0) {
                this.shipAgentLedgerService.addBalanceToShipAgent(userId, (-1 * agentBalance));
            }
            final Set<Ship> newShips = user.getShips();
            for (Ship ship : ships) {
                newShips.stream().filter(s -> s.getId() == ship.getId()).collect(Collectors.toSet());
            }
            user.getShips().clear();
//            user.setShips(newShips);
            return this.userRepo.save(user);
        }
        return null;
    }

    @Override
    public Page<User> findByRole(String role, int page) {
        return this.userRepo.findByRolesName(role, PageAttr.getPageRequest(page));
    }

    @Override
    public List<User> findByRole(String role) {
        return this.userRepo.findByRolesName(role);
    }

    @Override
    public List<User> getUserListByShipId(Long shipId) {
        return this.userRepo.findByShipsId(shipId);
    }

    @Override
    public Set<Ship> getUserShipList(Long userId) throws UserNotFoundException {
        User user = this.getOne(userId);
        return user.getShips();
    }


}
