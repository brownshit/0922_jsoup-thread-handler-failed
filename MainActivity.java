package com.practice.wpsactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button bt_1;
    private Button bt_2;

    //===============================================
        //Document doc;
    // Element doc = Jsoup.connect("https://api.ip.pe.kr/").get();
    Handler handler;
    Thread mtThread;

    // TODO: 2022-09-20  : Jsoup사용해서 웹페이지의 String 받아오면 되는데 이게 쓰레드를 사용해줘야 한다.
    String URL = "https://api.ip.pe.kr/";

    // TODO: 2022-09-21 보류
    //public_ip_get ip_get = new public_ip_get();

    //===============================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //어플 처음실행할때 실행되는 부분
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //===============================================

        final String[] myString = new String[1];
        final Document[] doc = {null};
        final Bundle bundle = new Bundle();



        //===============================================
        //https://api.ip.pe.kr/ 의 리턴값을 받아올 수 있도록

        bt_1 = findViewById(R.id.bt_1);
        bt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SubActivity1.class);
                startActivity(intent);
            }
        });

        handler = new Handler(Looper.getMainLooper()){
            String[] inner_public_ip;
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                mtThread.start();

                        /*
                        mtThread.start();
                        Bundle bundle = msg.getData();
                        bundle.getString("public_ip");
                        * */
                Bundle bundle = msg.getData();
                inner_public_ip[0] = bundle.getString("public_ip");

                //Bundle bundle = msg.getData(); // new Thread에서 작업한 결과물 받기
                //handleMessage(msg);
            }
        };

        mtThread = new Thread(){
            @Override
            public void run(){
                try {
                    Thread.sleep(1000);


                    //sendMessage?..
                    final String[] public_ip = {""};
                    //인텐트 하지말고 이부분에서 버튼을 클릭하면 도메인으로 넘어가게 화면을 구성하자.
                    //그러면 subactivity2가 필요없다.

                    //todo : thread running code
                    //return String, without Jsoup
                    /**
                     my_ip = public_ip_get.get_ip(URL);
                     message = handler.obtainMessage();
                     message.obj = (Object)my_ip;
                     handler.sendMessage(message);
                     handler.handlerMessage(message);

                     todo 위의 코드는 String 형을 message올 형변환 하는 코드
                     * **/
                    //크롤링 할 구문을 추가한다.
                    /**
                     * Document doc = Jsoup.connect(URL).get();
                     *                 Elements temele = doc.select("pre");	//끌어온 html에서 클래스네임이 "temperature_text" 인 값만 선택해서 빼오기
                     *                 Element targetelement = temele.get(1);
                     *                 String ip_text = targetelement.text();
                     *                 message = handler.handleMessage(targetelement.toString());
                     *
                     *                 //low compare
                     *                 bundle.putString("p_ip", temele); //bundle 이라는 자료형에 뽑아낸 결과값 담아서 main Thread로 보내기
                     *                 handler.handleMessage(temele);
                     * **/

                    //todo //handler밖으로 빼주려면 뭔가가 필요하다! 핸들러 안에서 돌아가기만 하고 처리가 안돼



                    /*
                     * TODO: 2022-09-22
                     *  thread에서 웹 페이지를 띄울거고, 핸들러로 url을 thread에 넘겨줄거다.
                     *
                     * */

                    //todo public_ip에 스레드에서 계산한 결과를 가져와줘야 한다.
                    //public_ip[0] = handler.obtainMessage().toString();
                    /**
                     * 위의 방식으로는 핸들러 그 자체를 가져온다. 핸들러에 대한 이해가 더 필요한듯 하다!
                     * **/


                    //todo public_ip를 domain_link로 만들어주기
                    final String domain_link = "https://" + inner_public_ip[0] + ":3000";

                    //todo 무조건 웹페이지 연결주소는 domain_link로.
                    Toast.makeText(MainActivity.this.getApplicationContext(),
                            domain_link.toString(),
                            Toast.LENGTH_SHORT).show();

                    // todo domain_link로 연결하는 코드
                    Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(domain_link)));
                    startActivity(webintent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        bt_2 = findViewById(R.id.bt_2);
        bt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    doc[0] = Jsoup.connect(URL).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //todo 아래의 두개
                Element content = doc[0].getElementById("pre");//.select("pre");
                Elements contents = doc[0].getAllElements();
                //todo element / elements


                myString[0] = content.text();

                //content.text();
                bundle.putString("public_ip", myString[0]);

                //todo 아래의 코드는 보내는 코드
                Message msg = handler.obtainMessage();  // handler의 메세지를 가져온다,,??!
                msg.setData(bundle);
                handler.sendMessage(msg);


                /*
                * todo
                *  여기 위의 trycatch문 안으로 버튼 클릭하면 동작하는
                *  모든 매서드들이 들어가야 하지 않을까 싶다!
                *  일단 Jsoup.connect에서 앱이 중지 된 걸로 봐서
                *  그 부분이 스레드 안으로 들어가야 앱이 중지되지 않을 것 같다.
                * */
                /**
                 doc = Jsoup.parse(connUrl.toString());
                 Elements element = doc.select("pre");
                 Element targetelement = element.get(1);
                 String ip_text = targetelement.text();

                 //이 부분을 실행하자.
                 * **/

                //String my_public_ip = doc.toString();

                    //todo
                //Toast.makeText(MainActivity.this, ip_text, Toast.LENGTH_SHORT).show();
                /**
                 https://api.ip.pe.kr/
                 * **/

                //바로 아래 도메인 링크가 연결되는 링크이다.
                //domain_link = "https://"+scanResult.SSID+":3000";
                //domain_link = "https://"+ipAddress+":3000";

                //domain_link[0] = "https://" + finalP_ipAdress + ":3000";
                    //todo

                // TODO: 2022-09-21 보류
                    //todo toString으로 받아오니 값이 이상하게 들어간다. 뭔가 잘못된게 있는 것 같다,


            }
        });

    }


}
