package 일반채팅;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Practice {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1231);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String localhost = inetAddress.getHostAddress();
        System.out.println(localhost);
//        serverSocket.bind(new InetSocketAddress(localhost, 222));
//        System.out.println(serverSocket.getInetAddress());
    }
}
