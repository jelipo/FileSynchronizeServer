package server.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by 10441 on 2016/10/25.
 */
public class FileService {

    public void del(JSONObject head, byte[] data) {
        System.out.println(head);
        JSONArray fileJsonArray = head.getJSONArray("fileKeyList");
        String parentPath = head.get("parentPath").toString();
        String os = System.getProperty("os.name");
        if (os.contains("Linux")) {
            deleteInLinux(fileJsonArray, parentPath);
        } else if (os.contains("Windows")) {
            deleteInWindows(fileJsonArray, parentPath);
        }


    }

    public void replace(JSONObject head, byte[] data) {
        JSONArray fileJsonArray = head.getJSONArray("fileKeyList");
        String parentPath = head.getString("parentPath");
        int ii = 0;
        for (int i = 0; i < fileJsonArray.size(); i++) {
            JSONObject singleFileJson = fileJsonArray.getJSONObject(i);
            File file  = new File(parentPath, singleFileJson.getString("path")+"/"+singleFileJson.getString("filename"));
            System.out.println(singleFileJson);
            int size = singleFileJson.getInteger("fileSize");
            byte[] singlefileByte = ArrayUtils.subarray(data, ii, ii + size);
            ii = ii + size;
            try {
                FileUtils.writeByteArrayToFile(file, singlefileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(JSONObject head, byte[] data) {
        JSONArray fileJsonArray = head.getJSONArray("fileKeyList");
        String parentPath = head.getString("parentPath");
        int ii = 0;
        for (int i = 0; i < fileJsonArray.size(); i++) {
            JSONObject singleFileJson = fileJsonArray.getJSONObject(i);
            File fileDe = new File(parentPath, singleFileJson.getString("path"));
            if (!fileDe.exists()) {
                fileDe.mkdirs();
            }
            File file = new File(parentPath, singleFileJson.getString("path")+"/"+singleFileJson.getString("filename"));
            int size = singleFileJson.getInteger("fileSize");
            byte[] singlefileByte = ArrayUtils.subarray(data, ii, ii + size);
            ii = ii + size;
            try {
                FileUtils.writeByteArrayToFile(file, singlefileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteInWindows(JSONArray fileJsonArray, String parentPath) {
        String cmdStr = "";
        for (int i = 0; i < fileJsonArray.size(); i++) {
            JSONObject singleFileJson = fileJsonArray.getJSONObject(i);
            parentPath=parentPath.replace("/","\\");
            String path=singleFileJson.getString("path").replace("/","\\");
            if (singleFileJson.getInteger("isFile")==1){
                cmdStr = cmdStr + "\"" + parentPath+ "\\" +path+ "\\"+ singleFileJson.getString("filename") + "\" ";
            }else{
                cmdStr = cmdStr + "\"" + parentPath+ "\\" +path+ "\\";
            }
        }
        try {
            cmdStr.replace("/", "\\");
            System.out.println("cmd /c rd/s/q " + cmdStr);
            Runtime.getRuntime().exec("cmd /c rd/s/q " + cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteInLinux(JSONArray fileJsonArray, String parentPath) {
        String linStr = "";
        for (int i = 0; i < fileJsonArray.size(); i++) {
            JSONObject singleFileJson = fileJsonArray.getJSONObject(i);
            if (singleFileJson.getInteger("isFile")==1){
                linStr = linStr + "'" + parentPath + "/" +singleFileJson.getString("path") + "/"+singleFileJson.getString("filename") + "' ";
            }else {
                linStr = linStr + "'" + parentPath + "/" +singleFileJson.getString("path") + "' ";
            }

        }
        try {
            linStr.replace("\\", "/");
            Runtime.getRuntime().exec("rm -rf " + linStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
