import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 10441 on 2016/10/8.
 */
public class SocketConnect implements Runnable {

    private Socket socket;

    public SocketConnect(Socket client) {
        this.socket = client;
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            BufferedReader in = null;
            while(!socket.isClosed()) {
                JSONObject json=new JSONObject();
                json.put("flag","msg");
                json.put("msg","我已经收到消息了！");
                in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String str = in.readLine();
                if(str==null){break;}
                System.out.println(str);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(json.toString());
                printWriter.flush();

            }


        } catch (IOException ex) {
            System.out.println("连接可能已经断开，此连接中断"+socket.getPort());

        } finally {

        }
    }
}
