package com.example.proline_appcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void check_inventory(View view) {
        Intent intent = new Intent(Dashboard.this, CheckInventory.class);
        startActivity(intent);
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        Intent intent = new Intent(Dashboard.this, WelcomeScreen.class);
        Dashboard.this.finish();
        startActivity(intent);

    }

    public void addProduct(View view) {
        Intent intent = new Intent(Dashboard.this, AddProduct.class);
        startActivity(intent);
    }

    public void bill(View view) {
        Intent intent = new Intent(Dashboard.this, Billing.class);
        startActivity(intent);
    }
}