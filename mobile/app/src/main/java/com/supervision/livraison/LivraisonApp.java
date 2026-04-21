package com.supervision.livraison;

import android.app.Application;

/**
 * Application entry point. Keeps a process-wide {@link SessionManager} so the
 * identity returned by {@code /api/auth/login} survives activity restarts.
 */
public class LivraisonApp extends Application {

    private static LivraisonApp instance;
    private SessionManager sessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sessionManager = new SessionManager(this);
    }

    public static LivraisonApp get() {
        return instance;
    }

    public SessionManager session() {
        return sessionManager;
    }
}
