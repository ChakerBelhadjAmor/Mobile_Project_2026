package com.supervision.livraison.repository;

import com.supervision.livraison.entity.Poste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosteRepository extends JpaRepository<Poste, String> {
}
