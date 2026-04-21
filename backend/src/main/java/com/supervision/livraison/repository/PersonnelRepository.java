package com.supervision.livraison.repository;

import com.supervision.livraison.entity.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonnelRepository extends JpaRepository<Personnel, Long> {
    Optional<Personnel> findByLogin(String login);
}
