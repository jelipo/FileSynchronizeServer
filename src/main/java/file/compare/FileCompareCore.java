package file.compare;

import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

/**
 * Created by 10441 on 2016/10/11.
 */
public class FileCompareCore {
    private JSONObject compareList=new JSONObject();
    private JSONObject needToReplaceList=new JSONObject();
    private Boolean isGetReplaceList;
 //                                server              client
    private void compare(JSONObject before, JSONObject after) {
        Iterator it = after.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            if (isFile(after.get(key))) {
                if (before.get(key) == null) {
                    compareList.put(key.toString(), after.get(key));
                } else if (isGetReplaceList) {
                    if (!(((JSONObject) after.get(key)).get("md5").equals(((JSONObject) before.get(key)).get("md5")))) {
                        needToReplaceList.put(key.toString(), after.get(key));
                    }
                }
            } else {
                compare((JSONObject) before.get(key), (JSONObject) after.get(key));
            }
        }
    }

    private static Boolean isFile(Object ob) {
        JSONObject json = (JSONObject) ob;
        if (json.get("isFile") == null) {
            return false;
        } else {
            return true;
        }
    }

    public JSONObject getCompareList(JSONObject before, JSONObject after, Boolean isGetReplaceList) {
        this.isGetReplaceList = isGetReplaceList;
        compare(before, after);
        return compareList;
    }

    public JSONObject getNeedToReplaceList(){
        return needToReplaceList;
    }

}
