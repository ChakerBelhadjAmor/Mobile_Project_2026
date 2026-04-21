package com.supervision.livraison.dto;

import com.supervision.livraison.entity.LigCde;
import com.supervision.livraison.entity.LivraisonCom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Flat representation of a delivery used on both mobile screens
 * (controller list and driver daily view).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivraisonDto {
    private Long    nocde;
    private LocalDate dateliv;
    private String  etatliv;
    private String  modepay;
    private String  remarque;

    private Long    livreurId;
    private String  livreurNom;

    private Long    clientId;
    private String  clientNom;
    private String  clientTel;
    private String  clientAdresse;
    private String  clientVille;
    private String  clientCodePostal;

    private Integer nbArticles;
    private Double  montantTotal;

    /** Build a DTO from the JPA entity graph. */
    public static LivraisonDto from(LivraisonCom l) {
        LivraisonDtoBuilder b = LivraisonDto.builder()
                .nocde(l.getNocde())
                .dateliv(l.getDateliv())
                .etatliv(l.getEtatliv())
                .modepay(l.getModepay())
                .remarque(l.getRemarque());

        if (l.getLivreur() != null) {
            b.livreurId(l.getLivreur().getIdpers())
             .livreurNom(safe(l.getLivreur().getPrenompers()) + " " + safe(l.getLivreur().getNompers()));
        }
        if (l.getCommande() != null && l.getCommande().getClient() != null) {
            var c = l.getCommande().getClient();
            b.clientId(c.getNoclt())
             .clientNom(safe(c.getPrenomclt()) + " " + safe(c.getNomclt()))
             .clientTel(c.getTelclt())
             .clientAdresse(c.getAdrclt())
             .clientVille(c.getVilleclt())
             .clientCodePostal(c.getCodePostal());
        }
        if (l.getCommande() != null && l.getCommande().getLignes() != null) {
            int nb = 0;
            double total = 0d;
            for (LigCde lg : l.getCommande().getLignes()) {
                nb += lg.getQtecde();
                if (lg.getArticle() != null && lg.getArticle().getPrixV() != null) {
                    total += lg.getQtecde() * lg.getArticle().getPrixV();
                }
            }
            b.nbArticles(nb).montantTotal(total);
        }
        return b.build();
    }

    private static String safe(String s) { return s == null ? "" : s; }
}
