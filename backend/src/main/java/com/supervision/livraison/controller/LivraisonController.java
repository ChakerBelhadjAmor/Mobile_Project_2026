package com.supervision.livraison.controller;

import com.supervision.livraison.dto.LivraisonDto;
import com.supervision.livraison.dto.UpdateEtatRequest;
import com.supervision.livraison.service.LivraisonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST endpoints for delivery queries and driver-side state updates.
 *
 * <ul>
 *   <li>GET  /api/livraisons            — controller search with filters</li>
 *   <li>GET  /api/livraisons/today      — controller real-time monitoring</li>
 *   <li>GET  /api/livraisons/driver/{id}/today — driver daily view</li>
 *   <li>GET  /api/livraisons/{nocde}    — detail of a delivery</li>
 *   <li>PUT  /api/livraisons/{nocde}/etat — driver updates state (+ remark)</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/livraisons")
public class LivraisonController {

    private final LivraisonService livraisonService;

    public LivraisonController(LivraisonService livraisonService) {
        this.livraisonService = livraisonService;
    }

    @GetMapping
    public List<LivraisonDto> search(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) Long   livreur,
            @RequestParam(required = false) Long   nocde) {
        return livraisonService.search(from, to, etat, livreur, nocde);
    }

    @GetMapping("/today")
    public List<LivraisonDto> today() {
        return livraisonService.findToday();
    }

    @GetMapping("/driver/{id}/today")
    public List<LivraisonDto> forDriverToday(@PathVariable Long id) {
        return livraisonService.findForDriverToday(id);
    }

    @GetMapping("/{nocde}")
    public LivraisonDto one(@PathVariable Long nocde) {
        return livraisonService.findOne(nocde);
    }

    @PutMapping("/{nocde}/etat")
    public LivraisonDto updateEtat(@PathVariable Long nocde,
                                   @RequestBody UpdateEtatRequest req) {
        return livraisonService.updateEtat(nocde, req);
    }
}
