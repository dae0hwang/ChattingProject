package 진짜이거다ㅇㅇ;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable {
    protected Socket sock;

    //이거 lcok처리 해줘야함.
    private static ArrayList<Socket> clients = new ArrayList(5);

    //각 클라이언트가 보낸 메세지 수
    private static ThreadLocal<Integer> sendNum = new ThreadLocal<>();

    Server(Socket sock) {
        this.sock = sock;
    }

    //락처리 필요.
    public void remove(Socket socket) {
        for (Socket s : Server.clients) {
            if (socket == s) {
                Server.clients.remove(socket);
                break;
            }
        }
    }

    //각 서버 소켓들 스레드 동시진행
    //클라한테 바이트 인풋하고 -> 스트링해석 -> 다시 클라한테 바이트 보냄.
    public void run() {
        sendNum.set(0);
        InputStream fromClient = null;
        OutputStream toClient = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        String name = null;

        try {
            System.out.println(sock + ": 연결됨");
            while (true) {
                //클라 메세지 인풋 스트림.
                fromClient = sock.getInputStream();
                dis = new DataInputStream(fromClient);

                //먼저 헤더 정보 받고, 정보 가져오기.
                byte[] header = new byte[8];
                dis.readFully(header, 0, 8);
                byte[] lengthBytes = Arrays.copyOfRange(header, 0, 4);
                byte[] typeBytes = Arrays.copyOfRange(header, 4, 8);
                //클라한테 받은 메세지 길이와 타입을 알았다.
                int length = byteArrayToInt(lengthBytes);
                int type = byteArrayToInt(typeBytes);

                //메세지 본내용 받기.
                byte[] receiveBytes = null;
                receiveBytes = new byte[length];
                dis.readFully(receiveBytes, 0, length);
                //String으로 전환된 receiveMessage
                String receiveMessage = new String(receiveBytes);
                System.out.println("서버 받앗는지 확인"+receiveMessage);

                //type이 1111이면 이름 등록.
                if (type == 1111) {
                    name = receiveMessage;
                }
                //아니라면 받은 메세지 다른 클라이언트에게 출력하기.
                else {
                    //보낸 스레드 수 증가
                    sendNum.set(sendNum.get() + 1);
                    //여기도 해더넣어야하네. 이곳에 헤더정보 입력.
                    //헤더정보 메세지길이 그리고 타입 3333=상대방글 4444=종료타입
                    byte[] serverHeader = new byte[8];
                    int serverLength = receiveBytes.length;
                    int servertypetype = 3333;
                    byte[] serverlengthBytes = intToByteArray(serverLength);
                    byte[] servertypeBytes = intToByteArray(servertypetype);
                    //헤더 메세지 완성.
                    serverHeader[0] = serverlengthBytes[0];
                    serverHeader[1] = serverlengthBytes[1];
                    serverHeader[2] = serverlengthBytes[2];
                    serverHeader[3] = serverlengthBytes[3];
                    serverHeader[4] = servertypeBytes[0];
                    serverHeader[5] = servertypeBytes[1];
                    serverHeader[6] = servertypeBytes[2];
                    serverHeader[7] = servertypeBytes[3];

                    //모두에게 보내기.
                    for (Socket s : clients) {
                        toClient = s.getOutputStream();
                        dos = new DataOutputStream(toClient);
                        dos.write(serverHeader, 0, 8);
                        dos.writeUTF(name);
                        dos.write(receiveBytes, 0, length);
                        dos.flush();
                        System.out.println(name + "이메세지 보냄");

                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(sock + ": 에러(" + ex + ")");
            System.out.println(name+"나갔음. ");

        } finally {
            try {
                if (sock != null) {
                    sock.close();
                    // 접속 후 나가버린 클라이언트인 경우 ArrayList에서 제거
                    remove(sock);
                }
//                fromClient = null;
//                toClient = null;
                System.out.println("4타입실행.");
                //타입 444 메세지 보냄.
                byte[] serverHeader = new byte[8];
                int serverLength = 0;
                int servertypetype = 4444;
                byte[] servertypeBytes = intToByteArray(servertypetype);
                byte[] serverLengthBytes = intToByteArray(serverLength);
                //헤더 메세지 완성.
                serverHeader[0] = serverLengthBytes[0];
                serverHeader[1] = serverLengthBytes[1];
                serverHeader[2] = serverLengthBytes[2];
                serverHeader[3] = serverLengthBytes[3];
                serverHeader[4] = servertypeBytes[0];
                serverHeader[5] = servertypeBytes[1];
                serverHeader[6] = servertypeBytes[2];
                serverHeader[7] = servertypeBytes[3];
                dos.write(serverHeader, 0, 8);
                //일단 이름과 보낸 개수만.
                for (Socket s : clients) {
                    toClient = s.getOutputStream();
                    dos = new DataOutputStream(toClient);
                    dos.writeUTF(name);
                    dos.writeInt(sendNum.get());
                    dos.flush();
                }
                fromClient = null;
                toClient = null;
            } catch (IOException ex) {
            }
        }
    }




    // 채팅 서버 메인
    public static void main(String[] args) throws IOException {
        ServerSocket serverSock = new ServerSocket(5510);
        System.out.println(serverSock + ": 서버소켓생성");
        while (true) {
            Socket client = serverSock.accept();
            // 접속한 클라이언트 상대용 Socket을 ArrayList에 저장
            clients.add(client);

            //Thread Start
            Server myServer = new Server(client);
            Thread serverThread = new Thread(myServer);
            serverThread.start();
        }
    }


    //인트를 4바이트배열로 변환
    public static byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte)(value >> 24);
        byteArray[1] = (byte)(value >> 16);
        byteArray[2] = (byte)(value >> 8);
        byteArray[3] = (byte)(value);
        return byteArray;
    }

    //4바이트 배열을 인트로 변환
    public static int byteArrayToInt(byte bytes[]) {
        return ((((int)bytes[0] & 0xff) << 24) |
            (((int)bytes[1] & 0xff) << 16) |
            (((int)bytes[2] & 0xff) << 8) |
            (((int)bytes[3] & 0xff)));
    }
}
