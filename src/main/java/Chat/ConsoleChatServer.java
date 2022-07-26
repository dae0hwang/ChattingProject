package Chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ConsoleChatServer extends Thread {
    protected Socket sock;
    private static ArrayList<Socket> clients = new ArrayList(5);

    // ------------------------------------
    // Constructor
    // ------------------------------------
    ConsoleChatServer(Socket sock) {
        this.sock = sock;
    }

    // ------------------------------------
    // ArrayList에서 클라이언트 소켓 제거
    // 접속 후 나가버리는 경우 클릉 쓸 때 오류가 발생
    // ------------------------------------
    public void remove(Socket socket) {
        for (Socket s : ConsoleChatServer.clients) {
            if (socket == s) {
                ConsoleChatServer.clients.remove(socket);
                break;
            }
        }
    }

    // ------------------------------------
    // Thread 실행 메소드
    // ------------------------------------
    public void run() {
        InputStream fromClient = null;
        OutputStream toClient = null;

        try {
            System.out.println(sock + ": 연결됨");

            fromClient = sock.getInputStream();

            byte[] buf = new byte[1024];
            int count;
            while ((count = fromClient.read(buf)) != -1) {
                for (Socket s : ConsoleChatServer.clients) {
                    if (sock != s) {
                        toClient = s.getOutputStream();
                        toClient.write(buf, 0, count);
                        toClient.flush();
                        System.out.println("서버가 클라한테보낸거 log");
                    }
                }
                System.out.write(buf, 0, count);
            }
        } catch (IOException ex) {
            System.out.println(sock + ": 에러(" + ex + ")");
        } finally {
            try {
                if (sock != null) {
                    sock.close();
                    // 접속 후 나가버린 클라이언트인 경우 ArrayList에서 제거
                    remove(sock);
                }
                fromClient = null;
                toClient = null;
            } catch (IOException ex) {
            }
        }
    }

    // ------------------------------------
    // 채팅 서버 메인
    // ------------------------------------
    public static void main(String[] args) throws IOException {
        ServerSocket serverSock = new ServerSocket(9999); // 9999번 포트에 서버생성
        System.out.println(serverSock + ": 서버소켓생성");
        while (true) {
            Socket client = serverSock.accept();

            // 접속한 클라이언트 상대용 Socket을 ArrayList에 저장
            clients.add(client);

            //---------------------------- Thread Start
            // 쓰레드는 run 메소드에서 accept로 만들어진 Socket을 이용하여
            // InputStream, OutputStream을 이용하여 글을 읽거나 쓴다.
            // -----------------------------------------------
            ConsoleChatServer myServer = new ConsoleChatServer(client);
            myServer.start();
        }
    }
}
