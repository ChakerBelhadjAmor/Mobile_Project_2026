package com.supervision.livraison.controller;

import com.supervision.livraison.entity.Personnel;
import com.supervision.livraison.repository.PersonnelRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Read-only personnel lookup endpoints — used by the controller UI to pick
 * a driver to filter on or to message.
 */
@RestController
@RequestMapping("/api/personnel")
public class PersonnelController {

    private final PersonnelRepository personnelRepository;

    public PersonnelController(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    /** Returns [{id, nom}] filtered by role ({@code LIVREUR} or {@code CONTROLEUR}). */
    @GetMapping
    public List<Map<String, Object>> list(@RequestParam(required = false) String role) {
        return personnelRepository.findAll().stream()
                .filter(p -> role == null
                        || (p.getPoste() != null && role.equalsIgnoreCase(p.getPoste().getLibelle())))
                .<Map<String, Object>>map(p -> Map.of(
                        "id",  (Object) p.getIdpers(),
                        "nom", (Object) (safe(p.getPrenompers()) + " " + safe(p.getNompers()))))
                .toList();
    }

    private static String safe(String s) { return s == null ? "" : s; }

    /** Minimal profile lookup. */
    @GetMapping("/{id}")
    public Personnel one(@PathVariable Long id) {
        return personnelRepository.findById(id).orElse(null);
    }
}
