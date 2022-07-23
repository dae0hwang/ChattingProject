package 쓰레드n명채팅;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConsoleChatServer implements Runnable {
    private Socket sock;
    private static ArrayList<Socket> clients = new ArrayList<>(5);

    public ConsoleChatServer(Socket sock) {
        this.sock = sock;
    }

    //ArrayList에서 클라이언트 소켓 제거
    //접속 후 나가버리는 경우 오류 발생
    public void remove(Socket socket) {
        for (Socket s : ConsoleChatServer.clients) {
            if (socket == s) {
                ConsoleChatServer.clients.remove(socket);
                break;
            }
        }
    }

    //Thread가 할일을 여기에 기술한다.
    @Override
    public void run() {
        InputStream fromClient = null;
        OutputStream toClient = null;

        try {
            System.out.println(sock + "연결됨");

            fromClient = sock.getInputStream();

            byte[] buf = new byte[1024];
            //여기서 클라이언트 버퍼 분석하면 되겠다.
            int count;
            //인풋스트림 들어온데이터 버퍼에 넣고 갯수 반환
            //-1이란 것은 클라가 완전히 종료했을 때를 의미
            //작성하지 않더라도 -1이 아니라, 그냥 대기 상태
            while ((count = fromClient.read(buf)) != -1) {
                for (Socket s : ConsoleChatServer.clients) {
                    if (sock != s) {
                        toClient = s.getOutputStream();
                        toClient.write(buf, 0, count);
                        toClient.flush();
                    }
                }
                //체크하기 pring랑
                System.out.write(buf, 0, count);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (sock != null) {
                    sock.close();
                    remove(sock);
                }
                fromClient = null;
                toClient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println(serverSocket + " 서버소켓 생성");

        while (true) {
            Socket client = serverSocket.accept();
            clients.add(client);
            ConsoleChatServer myServer = new ConsoleChatServer(client);
            Thread myServerThread = new Thread(myServer);
            myServerThread.start();
            //

        }
    }

}
