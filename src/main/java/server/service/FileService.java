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

    public void del(JSONObject head,byte[] data){
        JSONArray fileJsonArray = head.getJSONArray("fileKeyList");
        String parentPath = head.get("parentPath").toString();
        String cmdStr = "" ;
        for (int i = 0; i < fileJsonArray.size(); i++) {
            JSONObject singleFileJson = fileJsonArray.getJSONObject(i);
            cmdStr=cmdStr+"\""+parentPath+"\\"+singleFileJson.getString("filename")+"\" ";
        }
        try {
            cmdStr.replace("/","\\");
            Runtime.getRuntime().exec("cmd /c del "+cmdStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void replace(JSONObject head,byte[] data){
        JSONArray fileJsonArray=head.getJSONArray("fileKeyList");
        String parentPath=head.getString("parentPath");
        int ii=0;
        for (int i=0;i<fileJsonArray.size();i++){
            JSONObject singleFileJson=fileJsonArray.getJSONObject(i);
            File file=new File(parentPath,singleFileJson.getString("filename"));
            FileUtils.deleteQuietly(file.getAbsoluteFile());
            int size=singleFileJson.getInteger("fileSize");

            byte[] singlefileByte=ArrayUtils.subarray(data,ii,ii+size);
            ii=ii+size;
            try {
                FileUtils.writeByteArrayToFile(file,singlefileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(JSONObject head,byte[] data){
        JSONArray fileJsonArray=head.getJSONArray("fileKeyList");
        String parentPath=head.getString("parentPath");
        int ii=0;
        for (int i=0;i<fileJsonArray.size();i++){
            JSONObject singleFileJson=fileJsonArray.getJSONObject(i);
            File file=new File(parentPath,singleFileJson.getString("filename"));
            int size=singleFileJson.getInteger("fileSize");
            byte[] singlefileByte=ArrayUtils.subarray(data,ii,ii+size);
            ii=ii+size;
            try {
                FileUtils.writeByteArrayToFile(file,singlefileByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
