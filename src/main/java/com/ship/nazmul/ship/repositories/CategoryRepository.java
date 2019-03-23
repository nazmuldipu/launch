package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    Page<Category> findByShipId(Long id, Pageable pageable);

    List<Category> findByShipId(Long hotelId);
}
