package com.mungsuk.chat;

import static java.lang.System.out;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
//import java.net.URL;
//import java.net.URLConnection;

//import org.apache.http.util.ByteArrayBuffer;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

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
    private BufferedWriter networkWriter;
    private String ip = "";// IP
    private int port = 9999;// PORT번호

    @Override
    protected void onStop() {
        super.onStop();

        try{
            socket.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        mHandler = new Handler();

        setSocket.start();

        checkUpdate.start();

        final EditText et = (EditText) findViewById(R.id.EditText01);
        Button btn = (Button) findViewById(R.id.Button01);
        final TextView tv = (TextView) findViewById(R.id.TextView01);

        btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                out.println("전송버튼 눌림");
                if((et.getText().toString() != null
                        || !et.getText().toString().equals("")) && networkWriter != null) {
                    PrintWriter out = new PrintWriter(networkWriter,true);
                    String return_msg = et.getText().toString();
                    out.println(return_msg);
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
                while(true) {

                    Log.w("Chatting is running", "chatting is running");
                    html = networkReader.readLine();
                    out.println(html);
                    mHandler.post(showUpdate);
                }

            }catch(Exception e) {
            }
        }
    };

    private Runnable showUpdate = new Runnable() {
        public void run() {
            Toast.makeText(MainActivity.this, "Coming word: " + html,
                    Toast.LENGTH_SHORT).show();
        }
    };

    private Thread setSocket = new Thread() {
        public void run(){
            try {
                Log.w("Creating Socket", "소켓생성중....");
                socket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
                Log.w("Creating Socket", "소켓생성됨!!");
                networkWriter =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                networkReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }catch(IOException e) {
                out.println(e);
                e.printStackTrace();
            }
        }
    };
}

