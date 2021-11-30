package com.example.lifeplusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton music = findViewById(R.id.music);

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MusicActivity.class);//music으로 이동
                startActivity(intent);//액티비티 이동
            }
        });

        ImageButton weather = findViewById(R.id.weather);

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),WeatherActivity.class);//weather으로 이동
                startActivity(intent);//액티비티 이동
            }
        });

        ImageButton map = findViewById(R.id.map);

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MapActivity.class);//weather으로 이동
                startActivity(intent);//액티비티 이동
            }
        });

        ImageButton calorie = findViewById(R.id.calorie);

        calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CalActivity.class);//weather으로 이동
                startActivity(intent);//액티비티 이동
            }
        });
    }
}
