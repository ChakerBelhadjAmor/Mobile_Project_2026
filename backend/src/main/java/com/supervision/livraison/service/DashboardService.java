package com.supervision.livraison.service;

import com.supervision.livraison.dto.DashboardStatDto;
import com.supervision.livraison.repository.LivraisonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Aggregated metrics consumed by the controller dashboard.
 */
@Service
public class DashboardService {

    private final LivraisonRepository livraisonRepository;

    public DashboardService(LivraisonRepository livraisonRepository) {
        this.livraisonRepository = livraisonRepository;
    }

    public List<DashboardStatDto> countByLivreurAndEtat() {
        return livraisonRepository.countByLivreurAndEtat().stream()
                .map(r -> new DashboardStatDto(
                        ((Number) r[0]).longValue(),
                        (String)  r[1],
                        (String)  r[2],
                        ((Number) r[3]).longValue()))
                .toList();
    }

    public List<DashboardStatDto> countByClientAndEtat() {
        return livraisonRepository.countByClientAndEtat().stream()
                .map(r -> new DashboardStatDto(
                        ((Number) r[0]).longValue(),
                        (String)  r[1],
                        (String)  r[2],
                        ((Number) r[3]).longValue()))
                .toList();
    }
}
