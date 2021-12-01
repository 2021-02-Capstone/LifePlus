package com.example.lifeplusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class WeatherActivity extends AppCompatActivity {

    TextView result;
    String weather, finedust, humidity, temp, location;
    final Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String url = "https://weather.naver.com";
        result = findViewById(R.id.textView1);

        new Thread() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();

                    Element el_weather = doc.select(".weather.before_slash").first(); // 날씨
                    Element el_dustinfo = doc.select(".ttl_area em").first(); // 미세먼지
                    Element el_humidity = doc.select(".weather_area .desc").first(); // 습도
                    Element el_temp = doc.select(".weather_area strong").first(); // 기온
                    Element el_location = doc.select(".location_name").first(); // 현재 위치

                    weather = el_weather.text();
                    finedust = el_dustinfo.text();
                    humidity = el_humidity.text();
                    temp = el_temp.text().substring(5);
                    location = el_location.text();

                    bundle.putString("message", "현재 위치 : "+location+"\n"
                            +"오늘 날씨 : " + weather +"\n"
                            + "현재 기온 : " + temp + "\n"
                            + "미세먼지 : " + finedust + "\n" + "습도 : " + humidity
                            + "\n");

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            result.setText(bundle.getString("message"));
        }
    };

    public void onClick (View v){ // 새로고침 버튼 클릭 시
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}