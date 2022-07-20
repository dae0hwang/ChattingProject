package 쓰레드채팅;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class ServerHandler implements Runnable {
    Socket sock = null;
    public ServerHandler(Socket sock) {
        this.sock = sock;
    }
    @Override
    public void run() {
        InputStream fromServer = null;
        try {
            fromServer = sock.getInputStream();

            byte[] buf = new byte[1024];
            int count;

            while ((count = fromServer.read(buf)) != -1) {
                System.out.write(buf, 0, count);

            }
        } catch (IOException e) {
            System.out.println("연결 종료 (" + e + ")");
        }finally {
            try {
                if (fromServer != null) {
                    fromServer.close();
                }
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {
            }
        }

    }
}

public class ConsoleChatClient {
    public static void main(String[] args) {
        Socket sock = null;

        try {
            sock = new Socket("127.0.0.1", 9999);
            System.out.println(sock + ": 연결 됨");

            OutputStream toServer = sock.getOutputStream();

            //서버에서 지속적으로 메세지 받기위한 인풋 스레드드
            ServerHandler serverHandler = new ServerHandler(sock);
            Thread severHandlerThread = new Thread(serverHandler);
            severHandlerThread.start();

            //서버에 보내기
            byte[] buf = new byte[1024];
            int count;

            while ((count = System.in.read(buf)) != -1) {
                toServer.write(buf, 0, count);
                toServer.flush();
            }
        } catch (IOException e) {

        }finally {
            try {
                if (sock != null) {
                    sock.close();
                }
            } catch (IOException e) {

            }
        }
   }
}
