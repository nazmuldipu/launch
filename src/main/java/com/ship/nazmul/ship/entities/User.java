package com.ship.nazmul.ship.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ship.nazmul.ship.entities.base.BaseEntity;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "h_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "email"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseEntity implements UserDetails {
    @NotNull
    @NotEmpty
    private String name;

    @Column(unique = true, nullable = false)
    @NotNull
    @NotEmpty
    private String username;

    @Column
    @Email
    private String email;

    @Column(unique = true, nullable = false)
    @NotNull
    @Size(min = 11)
    private String phoneNumber;

    @NotEmpty
    @NotNull
    @Size(min = 6, max = 100, message = "Password must be between 6 to 100 characters!")
    @JsonIgnore
    @Column(length = 512, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.REMOVE}, fetch = FetchType.EAGER)
//    @(fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<Ship>();

    private boolean enabled = true;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private int commission;


//    public Hotel getHotel() {
//        return hotel;
//    }
//
//    public void setHotel(Hotel hotel) {
//        this.hotel = hotel;
//    }


    public User() {
    }

    public User(String name, String username, String phoneNumber, String password) {
        this.name = name;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    @PrePersist
    private void onPrePersist() {
//        if (roles == null || roles.isEmpty())
//            grantRole(new Role(Role.ERole.ROLE_USER));
        if (this.username==null) this.setUsername(this.getPhoneNumber());
    }


    public void grantRole(Role role) {
        if (this.roles == null)
            this.roles = new ArrayList<>();
        // check if user already has that role
        if (!hasRole(role.getRole()) && !role.isAdmin()){
            this.roles.add(role);}
        else if(role.isAdmin()){
            this.roles = new ArrayList<>();
            this.roles.add(role);
        }
    }

    public void changeRole(Role role) {
        if (role == null || role.getRole().equals(Role.ERole.ROLE_ADMIN.toString())) return;
        this.roles = new ArrayList<>();
        this.roles.add(role);
    }

    public boolean hasRole(String role) {
        return this.roles != null && this.roles.stream()
                .filter(r -> r.getRole().trim().equals(role.trim()))
                .count() > 0;
    }

    @JsonIgnore
    public boolean isOnlyUser() {
        return this.roles.size() == 1 && hasRole(Role.ERole.ROLE_USER.toString());
    }

//    @JsonIgnore
//    public boolean isLandLord() {
//        return this.hasRole(Role.ERole.ROLE_LANDLORD.toString());
//    }

//    @JsonIgnore
//    public boolean isNotAdminOrEmployeeOrFieldEmployee() {
//        return !this.hasRole(Role.ERole.ROLE_ADMIN.toString())
//                && !this.hasRole(Role.ERole.ROLE_EMPLOYEE.toString())
//                && !this.hasRole(Role.ERole.ROLE_FIELD_EMPLOYEE.toString());
//    }

//    @JsonIgnore
//    public boolean isEmployee() {
//        return this.hasRole(Role.ERole.ROLE_EMPLOYEE.toString());
//    }
//
//    @JsonIgnore
//    public boolean isEmployeeOrFieldEmployee() {
//        return this.hasRole(Role.ERole.ROLE_EMPLOYEE.toString()) || this.hasRole(Role.ERole.ROLE_FIELD_EMPLOYEE.toString());
//    }
//
//    @JsonIgnore
//    public boolean isFieldEmployee() {
//        return this.hasRole(Role.ERole.ROLE_FIELD_EMPLOYEE.toString());
//    }

    @JsonIgnore
    public boolean isAdmin() {
        return this.hasRole(Role.ERole.ROLE_ADMIN.toString());
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (Role role : this.roles) {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
            authorityList.add(authority);
        }
        return authorityList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", commission=" + commission +
                '}';
    }
}
