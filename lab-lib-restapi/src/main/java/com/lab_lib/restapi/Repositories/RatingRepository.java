package com.lab_lib.restapi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.RatingName;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findAllByBookId(Long bookId);
    Page<Rating> findAllByRnAndEvaluation(RatingName rn, Long evaluation, Pageable pageable);
    
}