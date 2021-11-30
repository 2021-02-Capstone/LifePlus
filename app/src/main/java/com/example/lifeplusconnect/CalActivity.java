package com.example.lifeplusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calmain);

        Button foodbut = (Button) findViewById(R.id.button1);
        foodbut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), foodcal.class);
                startActivity(intent);
            }
        });

        Button exercisebut = (Button) findViewById(R.id.button2);
        exercisebut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), exercisecal.class);
                startActivity(intent);
            }
        });
    }
}
