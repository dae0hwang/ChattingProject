package 객체직렬화;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ConnectException;
import java.net.Socket;

public class SerClient {
    public static void main(String[] args) {
        try {
            Socket s1 = new Socket("127.0.0.1", 5433);
            InputStream is = s1.getInputStream();
            ObjectInputStream dis = new ObjectInputStream(is);
            Employee p = (Employee) dis.readObject();
            System.out.println("이름: " + p.name);
            System.out.println("주소: " + p.addr);


        } catch (ConnectException connEXc) {
            System.out.println("연결 실패.");
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
