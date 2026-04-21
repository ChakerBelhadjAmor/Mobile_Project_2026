package com.supervision.livraison.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.supervision.livraison.LivraisonApp;
import com.supervision.livraison.SessionManager;
import com.supervision.livraison.databinding.ActivityLoginBinding;
import com.supervision.livraison.ui.controller.ControllerHomeActivity;
import com.supervision.livraison.ui.driver.DriverHomeActivity;
import com.supervision.livraison.viewmodel.LoginViewModel;

/**
 * Login entry point. Routes the user to the controller or driver home screen
 * based on the role returned by the backend.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private LoginViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        // Skip the form if a session is already valid from a previous launch.
        SessionManager session = LivraisonApp.get().session();
        if (session.isLoggedIn()) {
            routeByRole(session.getRole());
            return;
        }

        vm = new ViewModelProvider(this).get(LoginViewModel.class);

        vm.result().observe(this, resp -> {
            if (resp == null) return;
            Object idRaw = resp.get("idpers");
            if (idRaw == null) return;
            long   id     = ((Number) idRaw).longValue();
            String nom    = String.valueOf(resp.get("nom"));
            String prenom = String.valueOf(resp.get("prenom"));
            String role   = String.valueOf(resp.get("role"));
            session.save(id, nom, prenom, role);
            routeByRole(role);
        });

        vm.error().observe(this, msg -> {
            b.tvError.setText(msg);
            b.tvError.setVisibility(View.VISIBLE);
        });

        b.btnLogin.setOnClickListener(v -> {
            b.tvError.setVisibility(View.GONE);
            String login = String.valueOf(b.etLogin.getText());
            String pass  = String.valueOf(b.etPassword.getText());
            vm.login(login, pass);
        });
    }

    private void routeByRole(String role) {
        Intent next = SessionManager.ROLE_CONTROLLER.equalsIgnoreCase(role)
                ? new Intent(this, ControllerHomeActivity.class)
                : new Intent(this, DriverHomeActivity.class);
        startActivity(next);
        finish();
    }
}
