package com.lab_lib.restapi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lab_lib.restapi.Models.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findAllByBookId(Long bookId);
    Page<Rating> findAllByRatingNameAndEvaluation(String ratingName, int evaluation, Pageable pageable);
    boolean existsByBookIdAndRatingNameAndUserId(Long bookId, String ratingName, Long userId);

}