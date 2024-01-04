package com.mungsuk.chat;

import static java.lang.System.out;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient extends Activity {
    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }


    private String html = "";
    private Handler mHandler;
    private Socket socket;
    private String name;
    private BufferedReader networkReader;
    private PrintWriter networkWriter;
    //    private String ip = "127.0.0.1";// IP
    private String ip = "192.168.0.14";// IP
    private int port = 9999;// PORT번호
    private Thread update;
    private String nickName;
    TextView tv = null;

    @Override
    protected void onStop() {
        super.onStop();

        try {
            // 스레드를 안전하게 종료
            update.interrupt();
            update.join();

            // 소켓 닫기
            if (socket != null && socket.isConnected()) {
                networkWriter.println(nickName + "님이 퇴장하였습니다!");
                networkWriter.flush();
                socket.close();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this.getIntent());
        nickName = intent.getStringExtra("nick");


        // MainActivity에서 소켓 생성하기 위해선 필요한 코드
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        mHandler = new Handler();

        setSocket();
        networkWriter.println(nickName + "님이 접속하였습니다!");
        networkWriter.flush();

        checkUpdate.start();

        final EditText et = (EditText) findViewById(R.id.EditText01);
        Button btn = (Button) findViewById(R.id.Button01);
        tv = (TextView) findViewById(R.id.chatting);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                out.println("전송버튼 눌림");
                if((et.getText().toString() != null
                        || !et.getText().toString().equals("")) && networkWriter != null) {
//                    PrintWriter out = new PrintWriter(networkWriter,true);
                    String return_msg = et.getText().toString();
                    System.out.println("클라이언트가 보낸 메세지 : " + return_msg);
                    networkWriter.println(nickName + "님이 보낸 메세지 : " + return_msg);
                    networkWriter.flush();
                    et.setText("");
                } else if(networkWriter == null){
                    out.println("networkWriter가 null 임");
                }

            }
        });
    }


    private Thread checkUpdate = new Thread() {
        public void run() {
            try{
                String line = null;
                Log.w("ChattingStart", "Start Thread");
                while((line = networkReader.readLine()) != null) {
                    Log.w("Chatting is running", "chatting is running");

                    html = line;
                    out.println(html);
                    mHandler.post(showUpdate);
//                        tv.append(html);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tv.append(html + "\n");
                        }
                    });
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable showUpdate = new Runnable() {
        public void run() {
            Toast.makeText(ChatClient.this, "Coming word: " + html,
                    Toast.LENGTH_SHORT).show();
        }
    };


    public void setSocket(){
        try {
            Log.w("Creating Socket", "소켓생성중....");
            socket = new Socket(ip, port);
            if(socket != null){
                Log.w("Creating Socket", "소켓생성됨!!");
            } else{
                Log.w("Creating Socket", "소켓 생성중 오류생김");
            }

            networkWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "EUC_KR"));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "EUC_KR"));

        }catch(IOException e) {
            out.println(e);
            e.printStackTrace();
        }
    }
}
