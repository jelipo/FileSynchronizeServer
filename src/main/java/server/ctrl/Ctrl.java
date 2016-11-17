package server.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import server.service.FileService;
import server.temp.Temp;

import java.io.File;
import java.io.IOException;

/**
 * Created by 10441 on 2016/10/25.
 */

/**
 * 服务器返回给客户端json说明
 * {
 * "status":true/false ,   必须有，如果为true，则说明任务处理完毕，同时会通知客户端处理完毕,false时,服务器也会将此条传递给客户端,服务器也会继续循环等待
 * "msg":"消息内容"
 * }
 */
public class Ctrl {

    private JSONObject head;
    private byte[] data;
    private String ctrl;
    private Temp temp;

    public Ctrl(JSONObject head, byte[] data, Temp temp) {
        this.temp = temp;
        this.head = head;
        this.data = data;
        this.ctrl = head.getString("flag");
    }

    public JSONObject doSome() {
        switch (ctrl) {
            case "msg":
                return doMsg();
            case "file":
                return doFile();
            default:
                return JSON.parseObject("{'status':true,'msg','未找到可用命令'}");
        }
    }

    public JSONObject doMsg() {
        JSONObject socketJson = new JSONObject();
        socketJson.put("status", true);
        JSONObject json = new MsgCtrl().getReturnJson(head);
        socketJson.put("msg", "执行完成");
        socketJson.put("data", json);
        temp.setOver(true);
        return socketJson;
    }

    public JSONObject doFile() {
        FileService fileService = new FileService();
        System.out.println(head);
        switch (head.getString("need")) {
            case "needToDel":
                fileService.del(head,data);
            case "needToReplace":
                fileService.replace(head, data);
            case "needToAdd":
                fileService.add(head, data);
        }
        temp.setOver(true);
        JSONObject json = new JSONObject();
        json.put("status", true);
        json.put("msg", "“" + head.getString("need") + "”已完成");
        return json;
    }


}
