package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "personal_library",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ID"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalLibrary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

}