package com.supervision.livraison.service;

import com.supervision.livraison.dto.LivraisonDto;
import com.supervision.livraison.dto.UpdateEtatRequest;
import com.supervision.livraison.entity.LivraisonCom;
import com.supervision.livraison.repository.LivraisonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Business logic for deliveries — search, daily listing, driver scope,
 * and state updates.
 */
@Service
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;

    public LivraisonService(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }

    /** Controller search: any filter can be null. */
    @Transactional(readOnly = true)
    public List<LivraisonDto> search(LocalDate from, LocalDate to,
                                     String etat, Long livreurId, Long nocde) {
        return livraisonRepository.search(from, to, emptyToNull(etat), livreurId, nocde)
                .stream().map(LivraisonDto::from).toList();
    }

    /** Real-time monitoring: all today's deliveries. */
    @Transactional(readOnly = true)
    public List<LivraisonDto> findToday() {
        return livraisonRepository.findByDateliv(LocalDate.now())
                .stream().map(LivraisonDto::from).toList();
    }

    /** Driver daily tour. */
    @Transactional(readOnly = true)
    public List<LivraisonDto> findForDriverToday(Long driverId) {
        return livraisonRepository.findByLivreur_IdpersAndDateliv(driverId, LocalDate.now())
                .stream().map(LivraisonDto::from).toList();
    }

    @Transactional(readOnly = true)
    public LivraisonDto findOne(Long nocde) {
        return livraisonRepository.findById(nocde).map(LivraisonDto::from).orElse(null);
    }

    /**
     * Update the state of a delivery. When the state is {@code NON_LIVREE},
     * a non-blank {@code remarque} is required per the functional spec.
     *
     * @throws IllegalArgumentException if the delivery does not exist or
     *         the remark is missing for a non-delivered state.
     */
    @Transactional
    public LivraisonDto updateEtat(Long nocde, UpdateEtatRequest req) {
        LivraisonCom l = livraisonRepository.findById(nocde)
                .orElseThrow(() -> new IllegalArgumentException("Livraison introuvable: " + nocde));

        if ("NON_LIVREE".equalsIgnoreCase(req.getEtatliv())
                && (req.getRemarque() == null || req.getRemarque().isBlank())) {
            throw new IllegalArgumentException("Une remarque est obligatoire pour une livraison non livrée.");
        }

        l.setEtatliv(req.getEtatliv());
        l.setRemarque(req.getRemarque());
        return LivraisonDto.from(livraisonRepository.save(l));
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
