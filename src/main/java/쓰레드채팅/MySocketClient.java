package 쓰레드채팅;

import java.net.Socket;

public class MySocketClient {
    public static void main(String[] args) {
        try {
            Socket socket = null;
            socket = new Socket("127.0.0.1", 5510);
            System.out.println("서버 접속 성공");

            ListeningThread listeningThread = new ListeningThread(socket);
            Thread listenT1 = new Thread(listeningThread);
            WritingThread writingThread = new WritingThread(socket);
            Thread writeT1 = new Thread(writingThread);

            listenT1.start();
            writeT1.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
