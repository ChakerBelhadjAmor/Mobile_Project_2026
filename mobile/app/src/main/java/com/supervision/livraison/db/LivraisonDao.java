package com.supervision.livraison.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.supervision.livraison.model.Livraison;

import java.util.List;

/**
 * Room DAO for the driver's local delivery cache.
 * Exposes LiveData so the UI updates automatically when the repository
 * writes sync results back to the database.
 */
@Dao
public interface LivraisonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(List<Livraison> items);

    @Update
    void update(Livraison item);

    @Query("SELECT * FROM livraisons WHERE livreurId = :driverId ORDER BY etatliv, nocde")
    LiveData<List<Livraison>> byDriver(long driverId);

    @Query("SELECT * FROM livraisons WHERE nocde = :nocde LIMIT 1")
    LiveData<Livraison> byId(long nocde);

    @Query("SELECT * FROM livraisons WHERE nocde = :nocde LIMIT 1")
    Livraison byIdSync(long nocde);

    /** Rows with a pending offline edit; the repository pushes these on sync. */
    @Query("SELECT * FROM livraisons WHERE dirty = 1")
    List<Livraison> dirtyRows();
}
