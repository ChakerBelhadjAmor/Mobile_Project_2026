package com.supervision.livraison.controller;

import com.supervision.livraison.dto.DashboardStatDto;
import com.supervision.livraison.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Aggregated endpoints powering the controller dashboard screen.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/by-livreur")
    public List<DashboardStatDto> byLivreur() {
        return dashboardService.countByLivreurAndEtat();
    }

    @GetMapping("/by-client")
    public List<DashboardStatDto> byClient() {
        return dashboardService.countByClientAndEtat();
    }
}
