package 쓰레드채팅;

import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class MySocketServer implements Runnable {

    static ArrayList<Socket> list = new ArrayList<>();
    static Socket socket;

    public MySocketServer(Socket socket) {
        this.socket = socket;
        list.add(socket);
    }

    @Override
    public void run() {
        try {
            //연결확인용
            System.out.println("서버" + socket.getInetAddress() + " IP의 클라이언트와 연결됨");
            //수신자로부터온 메세지 버퍼 저장소
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            //서버에서 클라로 보내는 메세지 저장소
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);

            //서버 작성
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            //이름 입력해달라고함.
            writer.println("서버에 연결되었습니다. 이름을 등록해주세요");

            String readValue;
            String name = null;
            boolean identify = false;

            //클라이언트가 메세지 입력마다 수행
            while((readValue = reader.readLine()) != null ) {
                if (!identify) {
                    name = readValue;
                    identify = true;
                    writer.println(name + "님이 접속하였습니다.");
                    continue;
                }

                for (int i = 0; i < list.size(); i++) {
                    out = list.get(i).getOutputStream();
                    writer = new PrintWriter(out, true);
                    writer.println(name + ": " + readValue);
                }

                //서버가 보냄
                String str = br.readLine();
                for (int i = 0; i < list.size(); i++) {
                    out = list.get(i).getOutputStream();
                    writer = new PrintWriter(out, true);
                    writer.println("server" + ": " + str);
                }

            }

       } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
