package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartAppActivity extends AppCompatActivity {

    Button loginPage, homePage;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        loginPage = findViewById(R.id.moveToLogin);
        homePage = findViewById(R.id.moveToHomePage);

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartAppActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartAppActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}