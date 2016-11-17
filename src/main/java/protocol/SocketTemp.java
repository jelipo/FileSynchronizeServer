package protocol;

import com.alibaba.fastjson.JSONObject;
import server.temp.Temp;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 10441 on 2016/10/25.
 */
public class SocketTemp implements Runnable {
    private Temp temp;

    public SocketTemp(Temp temp) {
        this.temp = temp;
    }

    @Override
    public void run() {
        while (!(temp.getOver())) {
            try {
                Thread.sleep(1000);
                PrintWriter printWriter = null;
                JSONObject json=new JSONObject();
                json.put("status",false);
                json.put("msg",temp.getMsg());
                printWriter = new PrintWriter(temp.getSocket().getOutputStream());
                printWriter.println();
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
