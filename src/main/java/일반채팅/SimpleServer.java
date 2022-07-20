package 일반채팅;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) {
        InputStream is;
        OutputStream os;
        BufferedReader br_in;
        //내가 쓴것 버퍼저장소
        BufferedReader br_out;
        BufferedWriter bw;
        PrintWriter pw = null;
        ServerSocket serverSocket;
        Socket s1 = null;
        String inMessage = null;
        String outMessage = null;

        try {
            serverSocket = new ServerSocket(5510);
            System.out.println("서버 실행중..");
            s1 = serverSocket.accept();
            is = s1.getInputStream();
            os = s1.getOutputStream();

            //여기서 작성해서 내보낼거 버퍼저장소
            br_out = new BufferedReader(new InputStreamReader(System.in));
            //상대방이 보낸 것 저장소
            br_in = new BufferedReader(new InputStreamReader(is));

            //pw.println하면은 상대방한테 보냄 이정도 사실까지 파악함.
            bw = new BufferedWriter(new OutputStreamWriter(os));
            pw = new PrintWriter(bw, true);
            pw.println("Server : 접속을 환영합니다.");

            while (true) {
                inMessage = br_in.readLine();
                System.out.println(inMessage);

                outMessage = br_out.readLine();
                if (outMessage.equals("exit")) {
                    break;
                }
                pw.println("Server: " + outMessage);
            }
            pw.close();
            s1.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
