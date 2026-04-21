package com.supervision.livraison.repository;

import com.supervision.livraison.entity.LigCde;
import com.supervision.livraison.entity.LigCdeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigCdeRepository extends JpaRepository<LigCde, LigCdeId> {
    List<LigCde> findByCommande_Nocde(Long nocde);
}
