package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhoneNumber(String phoneNumber);

    Page<User> findByIdIn(List<Long> ids, Pageable pageable);

    Page<User> findByRolesName(String role, Pageable pageable);

    List<User> findByRolesName(String role);

    List<User> findByShipsId(Long shipsId);

    Page<User> findByUsernameContaining(String query, Pageable pageable);

//    @Query("SELECT DISTINCT u FROM User u WHERE u.ships.id = ?1 AND u.roles.name =?2")
    Page<User> findDistinctByShipsIdInAndRolesName(List<Long> shipsId, String role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:query% OR u.username LIKE %:query%")
    Page<User> searchByNameOrUsername(@Param("query") String query, Pageable pageable);

    Long countByCreatedBetween(Date fromDate, Date toDate);

    Page<User> findByNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCase(String name, String phoneNumber, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseAndRolesNameOrPhoneNumberContainingIgnoreCaseAndRolesName(String name, String role1, String phoneNumber, String role2, Pageable pageable);

//    Page<User> findByHotelIdAndRolesName(Long hotelId, String role, Pageable pageable);
//    List<User> findByHotelIdAndRolesName(Long hotelId, String role);

//    @Query("SELECT new com.monerbari.webservice.entities.pojo.DateCountPair(u.created, count(u.id)) FROM User u WHERE u.created BETWEEN :fromDate AND :toDate")
//    List<DateCountPair> countUsersInAPeriod(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
}