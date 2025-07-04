package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "publisher")
@Data // include getter/setter/toString/equals/hashCode
@NoArgsConstructor
@AllArgsConstructor
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;
}
