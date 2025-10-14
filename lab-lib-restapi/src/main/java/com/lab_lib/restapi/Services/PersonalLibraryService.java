package com.lab_lib.restapi.Services;

import com.lab_lib.restapi.DTO.PersonalLibrary.*;
import com.lab_lib.restapi.Repositories.PersonalLibraryRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import com.lab_lib.restapi.Models.PersonalLibrary;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PersonalLibraryService {
    
    @PersistenceContext
    private EntityManager entityManager;

    private final PersonalLibraryRepository personalLibraryRepository;

    public PersonalLibraryService(PersonalLibraryRepository personalLibraryRepository) {
        this.personalLibraryRepository = personalLibraryRepository;
    }

    @Transactional
    public List<PersonalLibrary> findAllByUserId(Long userId) {
        return personalLibraryRepository.findAllByUserId(userId);
    }

    @Transactional
    public synchronized PersonalLibrary addLibrary(AddLibraryRequest newLibrary) {

        String name = newLibrary.getName();
        Long userId = newLibrary.getUserId();

        //check per vedere se esiste già una libreria con lo stesso nome associata allo stesso utente
        if(personalLibraryRepository.existsByNameAndUserId(name, userId)) {
            throw new IllegalArgumentException("Personal library with the same name already exists");
        }

        PersonalLibrary library = new PersonalLibrary();
        library.setUserId(userId);
        library.setName(name);

        System.out.println("User id: " + userId + ";\nName: " + name);

        PersonalLibrary saved = personalLibraryRepository.save(library);
        return saved;

    }

}
