package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lab_lib.restapi.Models.PersonalLibrary;

public interface PersonalLibraryRepository extends JpaRepository<PersonalLibrary, Long> {
    
    boolean existsByName(String name);
    boolean existsByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
    List<PersonalLibrary> findAllByUserId(Long userId);

}
