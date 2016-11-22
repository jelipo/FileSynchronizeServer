package MySocket.core;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by 10441 on 2016/10/13.
 */

/**
 * socket自定义协议说明
 * /0!F/  (JSON)msg /0!E/
 *
 * /0!F/,/0!E/均为自定义协议中的分隔符
 * value传来时为JSON格式，为了避免传递时中文乱码的情况，传输之前，会对value进行base64编码
 */
public class SendSocket {

    private Socket socket;
    private JSONObject value;

    public SendSocket(Socket socket, JSONObject value) {
        this.socket = socket;
        this.value = value;
    }


    public void send() throws IOException {
        byte[] firstCut="/0!F/".getBytes();
        byte[] endCut="/0!E/".getBytes();
        byte[] valueByte= Base64.encodeBase64(value.toString().getBytes());
        byte[] allBytes= ArrayUtils.addAll(ArrayUtils.addAll(firstCut,valueByte),endCut);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.write(allBytes);
    }


}
