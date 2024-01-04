package com.mungsuk.chat;

import static java.lang.System.out;

//import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.BufferedReader;
//import java.io.BufferedWriter;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.ServerSocket;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button next = (Button) findViewById(R.id.Connect);
        final EditText nickName = (EditText) findViewById(R.id.NickName);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatClient.class);
                intent.putExtra("nick", String.valueOf(nickName.getText()));
                startActivity(intent);
            }
        });
    }

}
