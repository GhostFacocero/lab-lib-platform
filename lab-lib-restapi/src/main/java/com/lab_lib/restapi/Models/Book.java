package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private BigDecimal price;

    @Column(name = "publish_month")
    private Integer publishMonth;

    @Column(name = "publish_year")
    private Integer publishYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
