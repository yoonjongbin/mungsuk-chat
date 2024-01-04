package com.mungsuk.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TCPServer implements Runnable {

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    static List<PrintWriter> list = Collections.synchronizedList(new ArrayList<>());

    public TCPServer(Socket socket) {
        this.socket = socket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"EUC_KR"));
            out = new PrintWriter(socket.getOutputStream());
            list.add(out);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendAll(String msg){
        // 모든 클라이언트의 출력스트림을 통해서 readLine으로 읽은 메세지 전송
        for(PrintWriter out : list){
            out.println(msg);
            out.flush();
        }
    }


//        public static final String ServerIP = InetAddress.getLocalHost().getHostAddress();

        @Override
        public void run() {

//        String login = "[" + socket.getInetAddress() + "'s] is connected!!";

//        sendAll(login);

        while(in != null){
            String str;


            try{

                str = in.readLine();

                if(str == null) {
                    // 클라이언트와의 접속이 끊겼을때
                    synchronized (list) {
                        // 해당 클라이언트 출력스트림 제거
                        list.remove(out);
                    }
//                    System.out.println("[" + socket.getInetAddress() + "'s] is disconnected!");
//                    sendAll("[" + socket.getInetAddress() + "'s] is disconnected!");
                    break;
                }


//                System.out.println("[" + socket.getInetAddress() + "] sent Msg : " + str);
//                sendAll("[" + socket.getInetAddress() + "] sent Msg : " + str);
                System.out.println(str);
                sendAll(str);


            }catch (IOException e){
                e.printStackTrace();
                break;

            }

        }
//            try {
//
//
//
//                while (true) {
//                    Socket client = serverSocket.accept();
//                    System.out.println("S: Receiving...");
//
//                    try {
//                        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                        String str = null;
//                        while((str = in.readLine()) != null){
//                            System.out.println("S: Received : '" + client.getInetAddress() + "'s sendMsg ->" + str + "'");
//                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
//                            out.println("Server Received : " + str);
//                        }
//
//                    } catch(Exception e) {
//                        System.out.println("S: Error");
//                        e.printStackTrace();
//                    } finally {
//                        client.close();
//                        System.out.println("S: Done.");
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("S: Error");
//                e.printStackTrace();
//            }
        }

        public static void main(String[] args) {

            try{
                ServerSocket serverSocket = new ServerSocket(9999);
                System.out.println("S: Connecting...");

                while(true){
                    // 클라이언트가 새로 접속할때마다 해당 클라이언트의 소켓 정보를 담는 새로운 스레드 생성 및 실행
                    Socket socket = serverSocket.accept();
                    Thread server = new Thread(new TCPServer(socket));
                    server.start();
                }

            }catch (IOException e){
                e.printStackTrace();
            }


//            Thread desktopServerThread = new Thread(new TCPServer());
//            desktopServerThread.start();
        }
}