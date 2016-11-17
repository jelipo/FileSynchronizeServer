package server.ctrl;

import com.alibaba.fastjson.JSONObject;
import server.service.MsgService;

/**
 * Created by 10441 on 2016/10/25.
 */
public class MsgCtrl {
    private JSONObject value;
    public  MsgCtrl(){

    }
    public JSONObject getReturnJson(JSONObject value){
        this.value=value;
        switch (value.getString("msg")){
            case "getFileJson":return getFileStructure();
            default:return new JSONObject();
        }
    }
    private JSONObject  getFileStructure(){
        String path=value.getString("path");
        return new MsgService().getFileStructure(path);
    }
}
