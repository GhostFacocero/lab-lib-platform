package com.lab_lib.restapi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.RatingName;

@Repository
public interface RatingNameRepository extends JpaRepository<RatingName, Long> {
    
    boolean existsByName(String name);
    RatingName findIdByName(String name);

}
