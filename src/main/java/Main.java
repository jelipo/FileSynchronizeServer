import init.Context;
import protocol.MySocketProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final int PORT = 12345;

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(PORT);
        //创建线程池chachedThreadPool
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Context context = new Context();

        while (true) {
            // 一旦有堵塞, 则表示服务器与客户端获得了连接
            Socket client = serverSocket.accept();
            context.setCachedThreadPool(cachedThreadPool);
            // 处理这次新连接
            new MySocketProtocol(client,context);
            String ss[]="dsda".split("()");
        }
    }

}
