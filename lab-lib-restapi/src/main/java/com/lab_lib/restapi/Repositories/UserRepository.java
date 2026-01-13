package com.lab_lib.restapi.Repositories;

import com.lab_lib.restapi.Models.AppUser;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>{
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByToken(UUID token);
    AppUser findByToken(UUID token);
    AppUser findByEmail(String email);
    AppUser findByNickname(String nickname);
}
