package file;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by 10441 on 2016/10/9.
 */
public class FileTool {
    private String path;

    public FileTool(String path) {

        path = path.replace("\\", "/");
        String list[] = path.split("/");
        int length = list.length;
        String newpath = null;
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                newpath = list[i];
            } else {
                newpath = newpath + "/" + list[i];
            }
        }
        this.path = newpath;
    }

    public JSONObject getFileStructure() {
        File file = new File(path);
        if (file.isFile()) {
            file = file.getParentFile();
        }
        JSONObject json = null;
        try {
            json = Structure(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private JSONObject Structure(File file) throws IOException {
        File files[] = file.listFiles();
        JSONObject json = new JSONObject();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                json.put(files[i].getName(), fileJsonMark(files[i]));
            }
            if (files[i].isDirectory()) {
                json.put(files[i].getName(), Structure(files[i]));
            }
        }
        return json;
    }

    private JSONObject fileJsonMark(File file) throws IOException {
        JSONObject json = new JSONObject();
        json.put("isFile", 1);
        FileInputStream fileInputStream=new FileInputStream(file);

        json.put("md5", DigestUtils.md5Hex(fileInputStream));
        fileInputStream.close();
        json.put("path", file.getParentFile().toString().replace("\\", "/").replace(path, "/"));
        json.put("size", file.length());
        return json;
    }

}
