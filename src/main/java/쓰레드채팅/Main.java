package 쓰레드채팅;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        try {
            int socketPort = 5510;
            ServerSocket serverSocket = new ServerSocket(socketPort);
            System.out.println("socket" + socketPort + " 으로 서버가 열렸습니다.");

            while (true) {
                Socket socketUser = serverSocket.accept();
                MySocketServer mySocketServer = new MySocketServer(socketUser);
                Thread serverT1 = new Thread(mySocketServer);
                serverT1.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
