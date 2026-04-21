package com.supervision.livraison.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Delivery record. Doubles as the Retrofit DTO and the Room entity — the
 * server JSON shape is flat enough that a single class fits both layers
 * cleanly. The Room-only fields ({@code dirty}, {@code pendingEtat},
 * {@code pendingRemarque}) track offline changes until the next sync.
 */
@Entity(tableName = "livraisons")
public class Livraison {

    @PrimaryKey
    public long nocde;

    public String dateliv;          // ISO yyyy-MM-dd
    public String etatliv;
    public String modepay;
    public String remarque;

    public Long   livreurId;
    public String livreurNom;

    public Long   clientId;
    public String clientNom;
    public String clientTel;
    public String clientAdresse;
    public String clientVille;
    public String clientCodePostal;

    public Integer nbArticles;
    public Double  montantTotal;

    // --- Offline-first bookkeeping (not sent to the API) ---------------------

    /** True when the row has a pending edit not yet pushed to the backend. */
    public boolean dirty;
    public String  pendingEtat;
    public String  pendingRemarque;
}
