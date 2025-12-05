package com.lab_lib.restapi.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Models.Book;

public interface PersonalLibraryRepository extends JpaRepository<PersonalLibrary, Long> {

    @EntityGraph(attributePaths = "books")
    Optional<PersonalLibrary> findById(Long id);
    
    boolean existsByName(String name);
    boolean existsByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
    List<PersonalLibrary> findAllByUserId(Long userId);
    boolean existsByIdAndBooksId(Long plId, Long bookId);

}
