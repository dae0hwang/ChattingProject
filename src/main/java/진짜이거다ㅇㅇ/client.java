package 진짜이거다ㅇㅇ;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

// 서버에서 보내오는 값을 받기 위한 쓰레드
class ServerHandler implements Runnable {
    Socket sock = null;
    public ServerHandler(Socket sock) {
        this.sock = sock;
    }

    public void run() {
        InputStream fromServer = null;
        DataInputStream dis = null;
        try {
            while (true) {
                fromServer = sock.getInputStream();
                dis = new DataInputStream(fromServer);

                //먼저 헤더 정보 받고, 정보 가져오기.
                byte[] header = new byte[8];
                byte[] receiveBytes = null;
                dis.readFully(header, 0, 8);
                byte[] lengthBytes = Arrays.copyOfRange(header, 0, 4);
                byte[] typeBytes = Arrays.copyOfRange(header, 4, 8);
                //클라한테 받은 메세지 길이와 타입을 알았다.
                int length = byteArrayToInt(lengthBytes);
                ///333이면 상대방메세지, 4444이면 다른 사람 종료 메세지
                int type = byteArrayToInt(typeBytes);
                System.out.println("헤더받음");
                System.out.println("length" +length);
                System.out.println("type" + type);

                ///3333이면 이름과 스트링 메세지 받기.
                if (type == 3333) {
                    String name = dis.readUTF();
                    //메세지 본내용 받기.
                    receiveBytes = new byte[length];
                    dis.readFully(receiveBytes, 0, length);
                    //String으로 전환된 receiveMessage
                    String receiveMessage = new String(receiveBytes);
                    System.out.println(name + ": " + receiveMessage);
                }
                //else4444 일때는 이름과 메세지 보낸 갯수와 받은 갯수 받아서 출력.
               if (type == 4444) {
                   System.out.println("일로진입함");
                    String name = dis.readUTF();
                    int sendNum = dis.readInt();
                   System.out.println(name);
                   System.out.println(sendNum);
                    System.out.println(name+"이 나갔습니다 || 보낸 메세지 수: "+sendNum );
                }
            }
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




// 클라이언트 메인
// 보내는 기능 여기다가 구현.

public class client {
    public static void main(String[] args) throws IOException {
        Socket sock = null;
        try {
            sock = new Socket("localhost", 5510);

            //서버에서 보내오는 값을 받기위한 쓰레드
            ServerHandler chandler = new ServerHandler(sock);
            Thread receiveThread = new Thread(chandler);
            receiveThread.start();

            //처음 보낸 메세지만 패킷이 이름등록(1111)
            boolean first = true;
            int type;
            //접속시 이름동록 메세지 한번만 출력.
            System.out.print("이름을 먼저 등록 하세요 : ");


            while (true) {
                //발송을 위한 아웃풋 스트림
                OutputStream toServer = sock.getOutputStream();
                DataOutputStream dos = new DataOutputStream(toServer);

                //보낼메세지
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String sendMessage = br.readLine();
                byte[] bytes = sendMessage.getBytes("UTF-8");

                //헤더 구성 길이, 이름 or 글 인지.
                byte[] header = new byte[8];
                int length = bytes.length;
                byte[] lengthBytes = intToByteArray(length);
                if (first) {
                    type = 1111;
                } else {
                    //두번째 보낼때는 first = false ->2222(글쓰기타입)
                    type = 2222;
                }
                byte[] typeBytes = intToByteArray(type);
                //헤더 메세지 완성.
                header[0] = lengthBytes[0];
                header[1] = lengthBytes[1];
                header[2] = lengthBytes[2];
                header[3] = lengthBytes[3];
                header[4] = typeBytes[0];
                header[5] = typeBytes[1];
                header[6] = typeBytes[2];
                header[7] = typeBytes[3];

                //헤더 먼저 발송하기
                dos.write(header, 0, 8);
                //실제 내용(이름 or글 내용)
                dos.write(bytes, 0, bytes.length);
                dos.flush();
                //두번째 부터는 firs =false -> 헤더타입  = 2222(글쓰기)
                first = false;
            }
        } catch (IOException ex) {
            System.out.println("연결 종료 (" + ex + ")");
            System.out.println("나 나감");
        } finally {
            try {
                if (sock != null) {
                    sock.close();
                    System.out.println("나 나감");
                }
            } catch (IOException ex) {
            }
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

