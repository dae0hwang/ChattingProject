package 일반채팅;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPserver {

    public static final int PORT = 6077;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        OutputStream os = null;
        OutputStreamWriter osw = null;
        PrintWriter pw = null;
        Scanner sc = new Scanner(System.in);

        try {
            //1. 서버소켓 생성
            serverSocket = new ServerSocket();

            //2. 바인딩함
            //이거 그냥 포트번호로 지정함 생략가능
            InetAddress inetAddress = InetAddress.getLocalHost();
            String localhost = inetAddress.getHostAddress();
            serverSocket.bind(new InetSocketAddress(localhost, PORT));
            System.out.println("[server] binding " + localhost);

            //3. accept 클라이언트로부터 요청 기다림.
            //굳이 상대방 ip까지 알필요는 없음 전체적으로 대화내보내는 것이니깐
            Socket socket = serverSocket.accept();
            InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            System.out.println("[server] connected by client");
            System.out.println("[server] Connect with " + socketAddress.getHostString() + " " + socket.getPort());

            while (true) {
                // inputStream 가져와서 (주 스트림) StreamReader와 BufferedReader로 감싸준다 (보조 스트림)
                is = socket.getInputStream();
                isr = new InputStreamReader(is, "UTF-8");
                br = new BufferedReader(isr);

                // outputStream 가져와서 (주 스트림) StreamWriter와 PrintWriter로 감싸준다 (보조 스트림)
                os = socket.getOutputStream();
                osw = new OutputStreamWriter(os, "UTF-8");
                pw = new PrintWriter(osw, true);

                String buffer = null;
                buffer = br.readLine();
                if (buffer == null) {
                    System.out.println("[server] closed by client");
                    break;
                }

                System.out.println("[server] recived : " + buffer);
                pw.println(buffer);


            }
            //3 accept 클라이언트로부터 연결요청을 기다림
            //...blocking 되면서 기다리는 중 connect들어어면 block 풀림
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {

                if (serverSocket != null && !serverSocket.isClosed())
                    serverSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            sc.close();



        }
    }
}
