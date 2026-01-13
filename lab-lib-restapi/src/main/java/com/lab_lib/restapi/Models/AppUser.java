package com.lab_lib.restapi.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
    name = "app_user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nickname"}),
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"cf"}),
        @UniqueConstraint(columnNames = {"token"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, updatable = false, insertable = false)
    private UUID token;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(length = 16, unique = true)
    private String cf;

    @Column(length = 320, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
}
