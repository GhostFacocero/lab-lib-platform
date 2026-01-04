package com.lab_lib.restapi.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rating", uniqueConstraints = @UniqueConstraint(columnNames = {"id_book", "id_rn", "id_user"}))
@Getter
@Setter
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Integer id_book;

    @Column(unique = true)
    private Long id_rn;

    @Column(unique = true)
    private Long id_user;

    @Column
    private String review;

    @Column
    private Long evaliuation;
    
}
