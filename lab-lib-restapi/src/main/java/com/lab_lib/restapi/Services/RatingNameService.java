package com.lab_lib.restapi.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lab_lib.restapi.Models.RatingName;
import com.lab_lib.restapi.Repositories.RatingNameRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RatingNameService {
    
    @PersistenceContext
    private EntityManager entityManager;

    private final RatingNameRepository ratingNameRepository;

    public RatingNameService(RatingNameRepository ratingNameRepository) {
        this.ratingNameRepository = ratingNameRepository;
    }


    @Transactional
    public boolean existsByName(String name) {
        return ratingNameRepository.existsByName(name);
    }


    @Transactional
    public Long findIdByName(String name) {
        RatingName rn = ratingNameRepository.findIdByName(name);
        return rn.getId();
    }

}
