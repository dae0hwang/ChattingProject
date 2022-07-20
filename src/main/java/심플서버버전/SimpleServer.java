package 심플서버버전;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        BufferedWriter bw ;
        PrintWriter pw ;
        OutputStream os = null;
        ServerSocket serverSocket;
        Socket s1;
        InetAddress ipAddrs;
        String connectedClient;
        String outMessage = null;


        try {
            serverSocket = new ServerSocket(5432);
            System.out.println("서버 실행 중...");

            while (true) {
                //s1을 계속 찾는다.
                s1 = serverSocket.accept ();
                ipAddrs = s1.getInetAddress();
                connectedClient = ipAddrs.toString();

                bw = new BufferedWriter(new OutputStreamWriter(os));
                pw = new PrintWriter(bw, true);

                //connectedClient = s1의 ip이다.
                pw.println(connectedClient + "에서 서버에 접속하였습니다.");
                pw.close();
                s1.close();
                //여기까지 서버 기능 끝.


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
