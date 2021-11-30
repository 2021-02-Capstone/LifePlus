package com.example.lifeplusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MusicActivity extends AppCompatActivity {
    TextView result;
    final Bundle bundle = new Bundle();
    String url = null;
    Document doc = null; //doc = 전체 HTML 저장할 변수

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;

    Iterator<Element> ie1, ie2, ie3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        textView1 = (TextView) findViewById(R.id.music_Output1);
        textView2 = (TextView) findViewById(R.id.music_Output2);
        textView3 = (TextView) findViewById(R.id.music_Output3);
        textView4 = (TextView) findViewById(R.id.music_Output4);
        textView5 = (TextView) findViewById(R.id.music_Output5);

        EditText editText = (EditText) findViewById(R.id.music_Input);

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                url = "https://www.melon.com/search/lyric/index.htm?q=" + editText.getText().toString() + "=&searchGnbYn=Y&kkoSpl=Y&kkoDpType=&mwkLogType=T";
                //멜론 가사 검색 크롤링 주소 : https://www.melon.com/search/lyric/index.htm?q=검색어&searchGnbYn=Y&kkoSpl=Y&kkoDpType=&mwkLogType=T

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();

                            Elements element = doc.select("div.list_lyric"); //list_lyric 클래스 요소 선택

                            ie1 = element.select("span.atist>a").iterator(); //list_lyric 안의 span 요소인 atist 속의 a 태그 선택
                            ie2 = element.select("dl.cntt_lyric>dt>a.text").iterator(); //list_lyric 안의 span 요소인 album 속의 a 태그 선택
                            ie3 = element.select("dd.lyric>a").iterator(); //list_lyric 안의 dd 클래스인 lyric 속의 a 태그 선택

                            ArrayList<String> arrayList = new ArrayList<>();
                            for(int i=0; i<5; i++)
                                arrayList.add(ie1.next().text() + "\n" + ie2.next().text() + "\n" + ie3.next().text());

                            bundle.putStringArrayList("array", arrayList);

                            Message msg = handler.obtainMessage();
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        } catch (IOException e) {
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
            ArrayList<String> arrayList = bundle.getStringArrayList("array");
            textView1.setText(arrayList.get(0));
            textView2.setText(arrayList.get(1));
            textView3.setText(arrayList.get(2));
            textView4.setText(arrayList.get(3));
            textView5.setText(arrayList.get(4));
            //textView.setText(editText.getText());
        }
    };

}