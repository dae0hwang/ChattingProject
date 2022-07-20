package 심플서버버전;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleClient {
    public static void main(String[] args) {
        InputStream is;
        BufferedReader br;
        String message = null;

        try {
            Socket s1 = new Socket("127.0.0.1", 5432);
//            is = s1.getInputStream();
//            br = new BufferedReader(new InputStreamReader(is));
//            message = br.readLine();
//            System.out.println(message);
            System.out.println("연결성공");
            s1.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
    }
}}

