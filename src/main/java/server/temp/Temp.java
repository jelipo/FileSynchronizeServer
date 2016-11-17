package server.temp;

import com.alibaba.fastjson.JSONObject;

import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created by 10441 on 2016/10/25.
 */
public class Temp {
    public JSONObject returnJson;
    public Boolean isOver=false;
    public String msg;
    private ExecutorService cachedThreadPool;

    public ExecutorService getCachedThreadPool() {
        return cachedThreadPool;
    }

    public void setCachedThreadPool(ExecutorService cachedThreadPool) {
        this.cachedThreadPool = cachedThreadPool;
    }

    public synchronized Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket socket;
    public JSONObject getReturnJson() {
        return returnJson;
    }

    public void setReturnJson(JSONObject returnJson) {
        this.returnJson = returnJson;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
