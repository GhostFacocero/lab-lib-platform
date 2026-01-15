package com.lab_lib.restapi.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "rating", uniqueConstraints = @UniqueConstraint(columnNames = {"id_book", "id_rn", "id_user"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_book")
    @JsonBackReference
    private Book book;

    @ManyToOne
    @JoinColumn(name = "id_rn")
    private RatingName name;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private AppUser user;

    @Column
    private String review;

    @Min(1)
    @Max(5)
    @Column
    private Integer evaluation;
    
}
