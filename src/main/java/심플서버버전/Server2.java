//package 심플서버버전;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class Server2 extends Thread {
//
//    final static int SERVER_PORT = 1225;
//    final static String MESSAGE_TO_SERVER = "Hello, Client";
//
//    public static void main(String[] args) {
//
//        ServerSocket serverSocket = null;
//
//        try {
//            serverSocket = new ServerSocket(SERVER_PORT);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            while (true) {
//                System.out.println("socket 연결 대기");
//                Socket socket = serverSocket.accept();
//                System.out.println("host : "+socket.getInetAddress()+" | 통신 연결 성공");
//
//                /**	Server에서 보낸 값을 받기 위한 통로 */
//                InputStream is = socket.getInputStream();
//                /**	Server에서 Client로 보내기 위한 통로 */
//                OutputStream os = socket.getOutputStream();
//
//                byte[] data = new byte[16];
//                int n = is.read(data);
//                final String messageFromClient = new String(data,0,n);
//
//                System.out.println(messageFromClient);
//
//                os.write( MESSAGE_TO_SERVER.getBytes() );
//                os.flush();
//
//                is.close();
//                os.close();
//                socket.close();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
//
//class SocketRun implements Runnable {
//
//    private Socket socket = null;
//
//    SocketRun( Socket socket ){
//        this.socket = socket;
//    }
//
//    @Override
//    public void run() {
//
//    }
//}

package 심플서버버전;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2 extends Thread {
    final static int SERVER_PORT = 1225;
    final static String MESSAGE_TO_SERVER = "Hello Client";

    public static void main(String[] args) {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                System.out.println("socket 연결 대기 ");
                Socket socket = serverSocket.accept();
                System.out.println("host" +socket.getInetAddress() + " |통신 연결 성공");

                //Server에서 보낸 값을 받기 위한 통로
                InputStream is = socket.getInputStream();
                //Server에서 Client로 보내기 위한 통로
                OutputStream os = socket.getOutputStream();

                byte[] data = new byte[16];
                int n = is.read(data);
                final String messageFromClient = new String(data, 0, n);

                System.out.println(messageFromClient);

                os.write(MESSAGE_TO_SERVER.getBytes());
                os.flush();

                is.close();
                os.close();
                socket.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class SocketRun implements Runnable {

    private Socket socket = null;

    SocketRun( Socket socket ){
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
