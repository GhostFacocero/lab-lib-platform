package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lab_lib.restapi.Models.PersonalLibrary;
import com.lab_lib.restapi.Models.Book;

public interface PersonalLibraryRepository extends JpaRepository<PersonalLibrary, Long> {
    
    boolean existsByName(String name);
    boolean existsByUserId(Long userId);
    boolean existsByNameAndUserId(String name, Long userId);
    List<PersonalLibrary> findAllByUserId(Long userId);
    
    @Query("""
    SELECT COUNT(*) > 0
    FROM PersonalLibrary pl JOIN pl.books b
    WHERE pl.id = :idPl
    AND b.id = :idBook
    """)
    boolean existsByIdPlAndIdBook(@Param("idPl") Long idPl, @Param("idBook") Long idBook);

    @Query("""
    SELECT b.id
    FROM PersonalLibrary pl JOIN pl.books b
    WHERE pl.id = :idPl
            """)

    List<Book> findBooksById(@Param("idPl") Long idPl);

}
