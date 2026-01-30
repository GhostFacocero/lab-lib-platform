// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

package com.lab_lib.restapi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lab_lib.restapi.Models.RatingName;

@Repository
public interface RatingNameRepository extends JpaRepository<RatingName, Long> {
    
    boolean existsByName(String name);
    RatingName findIdByName(String name);

}
