package com.supervision.livraison.repository;

import com.supervision.livraison.entity.LivraisonCom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * JPA repository for deliveries. Exposes the filtered queries consumed by the
 * controller dashboard and real-time monitoring screens.
 */
public interface LivraisonRepository extends JpaRepository<LivraisonCom, Long> {

    /** All deliveries scheduled on the given day. */
    List<LivraisonCom> findByDateliv(LocalDate date);

    /** Deliveries for a driver on the given day — used by the driver home screen. */
    List<LivraisonCom> findByLivreur_IdpersAndDateliv(Long idpers, LocalDate date);

    /** Flexible filtered lookup used by the controller "full list" screen. */
    @Query("SELECT l FROM LivraisonCom l " +
           "WHERE (:from    IS NULL OR l.dateliv >= :from) " +
           "  AND (:to      IS NULL OR l.dateliv <= :to) " +
           "  AND (:etat    IS NULL OR l.etatliv = :etat) " +
           "  AND (:livreur IS NULL OR l.livreur.idpers = :livreur) " +
           "  AND (:nocde   IS NULL OR l.nocde = :nocde) " +
           "ORDER BY l.dateliv DESC, l.nocde DESC")
    List<LivraisonCom> search(@Param("from")    LocalDate from,
                              @Param("to")      LocalDate to,
                              @Param("etat")    String etat,
                              @Param("livreur") Long livreur,
                              @Param("nocde")   Long nocde);

    /** Aggregation: number of deliveries grouped by driver and state. */
    @Query("SELECT l.livreur.idpers, " +
           "       CONCAT(l.livreur.prenompers, ' ', l.livreur.nompers), " +
           "       l.etatliv, COUNT(l) " +
           "FROM LivraisonCom l " +
           "WHERE l.livreur IS NOT NULL " +
           "GROUP BY l.livreur.idpers, l.livreur.prenompers, l.livreur.nompers, l.etatliv")
    List<Object[]> countByLivreurAndEtat();

    /** Aggregation: number of deliveries grouped by client and state. */
    @Query("SELECT l.commande.client.noclt, " +
           "       CONCAT(l.commande.client.prenomclt, ' ', l.commande.client.nomclt), " +
           "       l.etatliv, COUNT(l) " +
           "FROM LivraisonCom l " +
           "GROUP BY l.commande.client.noclt, l.commande.client.prenomclt, l.commande.client.nomclt, l.etatliv")
    List<Object[]> countByClientAndEtat();
}
