package com.lab_lib.restapi.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lab_lib.restapi.Models.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Optional<Rating> findById(Long id);

}
