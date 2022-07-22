package Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

//----------------------------------
// 서버에서 보내오는 값을 받기 위한 쓰레드
//----------------------------------
class ServerHandler extends Thread {
    Socket sock = null;

    public ServerHandler(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        InputStream fromServer = null;
        try {
            fromServer = sock.getInputStream();

            byte[] buf = new byte[1024];
            int count;

            while ((count = fromServer.read(buf)) != -1)
                System.out.write(buf, 0, count);

        } catch (IOException ex) {
            System.out.println("연결 종료 (" + ex + ")");
        } finally {
            try {
                if (fromServer != null)
                    fromServer.close();
                if (sock != null)
                    sock.close();
            } catch (IOException ex) {
            }
        }
    }
}

//------------------------------
// 클라이언트 메인
//------------------------------
public class ConsoleChatClient {
    public static void main(String[] args) throws IOException {
        Socket sock = null;
        try {
            sock = new Socket("localhost", 9999);
            System.out.println(sock + ": 연결됨");
            OutputStream toServer = sock.getOutputStream();

            // ---- 서버에서 보내오는 값을 받기위한 쓰레드
            ServerHandler chandler = new ServerHandler(sock);
            chandler.start();

            byte[] buf = new byte[1024];
            int count;
            // ---- 콘솔에서 읽은 값을 서버에 보냄
            // 키보드 입력을 바이트 단위로 읽어 buf 라는 바이트배열에 기록 후 읽은 바이트수를 리턴
            // 읽을것이 없는 경우 read 메소드는 대기하고 Ctrl + C등 눌러 종료하면 -1이 리턴한다.
            // 스트림의 끝인 경우 -1을 리턴한다.
            while ((count = System.in.read(buf)) != -1) {
                toServer.write(buf, 0, count);
                toServer.flush();
            }
        } catch (IOException ex) {
            System.out.println("연결 종료 (" + ex + ")");
        } finally {
            try {
                if (sock != null)
                    sock.close();
            } catch (IOException ex) {
            }
        }
    }
}
