// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.Rating;
import com.lab_lib.restapi.Models.RatingName;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    List<Rating> findAllByBookId(Long bookId);
    
    boolean existsByBookIdAndNameAndUserId(Long bookId, RatingName name, Long userId);

}