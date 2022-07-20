package 쓰레드채팅;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ListeningThread implements Runnable {
    Socket socket = null;

    public ListeningThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while (true) {
                System.out.println(reader.readLine());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
