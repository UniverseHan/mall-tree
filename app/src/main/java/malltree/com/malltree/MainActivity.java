package malltree.com.malltree;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    String urlAddress = "http://192.168.0.5:8080/"; // 웹사이트 주소를 저장할 변수
    Handler handler = new Handler(); // 화면에 그려주기 위한 핸들러 객체

    //demo_view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 웹에서 html 읽어오기
        //    1. 인터넷 권한 얻어오기 AndroidManifest.xml
        //    2. 쓰레드를 작성
        //    3. Handler 객체를 통해야만 화면을 그릴수 있다

        tv = (TextView)findViewById(R.id.demo_view);
        Button b = (Button)findViewById(R.id.button_demo);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHtml(); // 웹에서 html 읽어오기
            }
        });

    }

    void loadHtml() { // 웹에서 html 읽어오기
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();

                try {
                    URL url = new URL(urlAddress);
                    HttpURLConnection conn =
                            (HttpURLConnection)url.openConnection();// 접속
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode()
                                ==HttpURLConnection.HTTP_OK){
                            //    데이터 읽기
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(),"euc-kr"));//"utf-8"
                            while(true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line+"\n");
                            }
                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(sb.toString());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
    }



}
