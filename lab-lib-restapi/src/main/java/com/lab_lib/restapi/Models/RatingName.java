package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rating_name")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingName {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating", unique = true)
    private String name;

}