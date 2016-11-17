package server.service;

import com.alibaba.fastjson.JSONObject;
import file.FileTool;

/**
 * Created by 10441 on 2016/10/25.
 */
public class MsgService {

    public  JSONObject getFileStructure(String path) {

        return new FileTool(path).getFileStructure();
    }
}
