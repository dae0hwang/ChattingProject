package 객체직렬화;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SerServer {
    public static void main(String[] args) {
        ServerSocket s = null;
        try {
            s = new ServerSocket(5433);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                System.out.println("서버 실행 중");
                Socket s1 = s.accept();
                OutputStream out = s1.getOutputStream();
                ObjectOutputStream dos = new ObjectOutputStream(out);
                Employee p = new Employee("박지성", "서울시 강남구",
                    "1111111-111111", "123-123");
                dos.writeObject(p);
                dos.close();
                s1.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
