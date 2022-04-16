package com.example.proline_appcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    }

    public void signUp_ws(View view) {
        Intent intent = new Intent(WelcomeScreen.this, SignUp.class);
        startActivity(intent);
    }

    public void signIn_ws(View view) {
        Intent intent = new Intent(WelcomeScreen.this, LogIn.class);
        startActivity(intent);
    }
}