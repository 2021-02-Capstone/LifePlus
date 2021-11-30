package com.example.lifeplusconnect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lifeplusconnect.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class foodcal extends AppCompatActivity {
    TextView result;
    String url = null;
    String msg;
    final Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodcal);
        result = findViewById(R.id.foodresult);

        EditText editText1 = (EditText) findViewById(R.id.food_Input);
        Button button = (Button) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.dietshin.com/calorie/calorie_search.asp?keyword=" + editText1.getText().toString();
                new Thread(){
                    @Override
                    public void run(){
                        Document doc = null;
                        try{
                            doc = Jsoup.connect(url).get();
                            Elements contents = doc.select("table.tbl-list.ty02 tbody");

                            for(Element content : contents){
                                Elements tdContents = content.select("tr");

                                String str, str1, str2, str3, str4, str5, str6, str7, str8;
                                //0번째 1번째 2번째배열에 넣어서

                                str = tdContents.get(0).text(); // 검색 결과에 대한 1순위 결과
                                str1 = tdContents.get(1).text(); // 2순위
                                str2 = tdContents.get(2).text(); // 3순위
                                str3 = tdContents.get(3).text(); // 4순위
                                str4 = tdContents.get(4).text(); // 5순위
                                str5 = tdContents.get(5).text(); // 6순위
                                str6 = tdContents.get(6).text(); // 7순위
                                str7 = tdContents.get(7).text(); // 8순위
                                str8 = tdContents.get(8).text(); // 9순위

                                msg = str + "\n" + str1 + "\n" + str2 + "\n" +  str3 + "\n"
                                        + str4 + "\n" + str5 + "\n" + str6 + "\n" +  str7 + "\n" + str8 ;
                            }

                            //핸들러를 이용하여 Thread()에서 가져온 데이터를 메인 스레드에 보내줌
                            bundle.putString("message",msg);
                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            result.setText(bundle.getString("message"));
        }
    };
}
