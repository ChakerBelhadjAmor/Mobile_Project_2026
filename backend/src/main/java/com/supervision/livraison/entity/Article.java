package com.supervision.livraison.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Article sold by the company.
 * Maps the {@code Articles} table of BDG_LivraisonCom_25.
 */
@Entity
@Table(name = "articles")
@Getter
@Setter
@NoArgsConstructor
public class Article {

    /** Reference code of the article (primary key). */
    @Id
    @Column(name = "refart", length = 20)
    private String refart;

    /** Human-readable label of the article. */
    @Column(name = "designation", nullable = false, length = 150)
    private String designation;

    /** Purchase price. */
    @Column(name = "prix_a")
    private Double prixA;

    /** Sale price. */
    @Column(name = "prix_v")
    private Double prixV;

    /** VAT code applied to this article. */
    @Column(name = "codetva", length = 10)
    private String codetva;

    /** Category of the article. */
    @Column(name = "categorie", length = 50)
    private String categorie;

    /** Quantity currently available in stock. */
    @Column(name = "qtestk")
    private Integer qtestk;
}
