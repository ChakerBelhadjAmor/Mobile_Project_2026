package com.supervision.livraison;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Tiny wrapper over {@link SharedPreferences} that holds the authenticated
 * user identity (id, name, role). Used to branch the app between the
 * controller and driver flows and to stamp outgoing requests.
 */
public class SessionManager {

    public static final String ROLE_CONTROLLER = "CONTROLEUR";
    public static final String ROLE_DRIVER     = "LIVREUR";

    private static final String PREFS = "livraison.session";
    private final SharedPreferences prefs;

    public SessionManager(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(long id, String nom, String prenom, String role) {
        prefs.edit()
                .putLong(  "id",     id)
                .putString("nom",    nom)
                .putString("prenom", prenom)
                .putString("role",   role)
                .apply();
    }

    public void clear() { prefs.edit().clear().apply(); }

    public long   getUserId()   { return prefs.getLong("id", -1L); }
    public String getFullName() { return prefs.getString("prenom", "") + " " + prefs.getString("nom", ""); }
    public String getRole()     { return prefs.getString("role", ""); }

    public boolean isLoggedIn() { return getUserId() > 0; }
}
