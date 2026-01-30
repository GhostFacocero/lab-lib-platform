// Emanuele Contini, matricola 756441
// Emanuele Gobessi, matricola 757599
// Diego Guidi, matricola 758420
// Nicola Curchi, matricola 757786
// Mirko Gurzau, matricola 757925

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
