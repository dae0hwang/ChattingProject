package 쓰레드채팅;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Scanner;

public class WritingThread implements Runnable {
    Socket socket = null;
    //작성용
    Scanner scanner = new Scanner(System.in);

    public WritingThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            while (true) {
                writer.println(scanner.nextLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
