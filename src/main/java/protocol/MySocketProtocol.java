package protocol;

import MySocket.core.SendSocket;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import init.Context;
import org.apache.commons.codec.binary.Base64;
import server.ctrl.Ctrl;
import server.temp.Temp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 10441 on 2016/10/15.
 */
public class MySocketProtocol implements Runnable {
    private Socket socket;
    private Context context;
    private String firsFlag="/0!F/";
    private int[] FIRST_BYTE ;
    private int FIRST_LENGTH;
    private int[] first ;
    private int first_length;
    private String endFlag="/0!H/";
    private int[] END_BYTE ;
    private int END_LENGTH;
    private int[] end ;
    private int end_length;
    private String overFlag="/0!E/";

    private long timeA=0;


    public MySocketProtocol(Socket client, Context context) {
        this.socket = client;
        this.context=context;
        new Thread(this).start();
    }

    private void init(){
        byte[] by=firsFlag.getBytes();
        FIRST_BYTE=new int[by.length];
        for(int i=0;i<by.length;i++){
            FIRST_BYTE[i]=by[i];
        }
        FIRST_LENGTH=FIRST_BYTE.length;
        first=new int[FIRST_LENGTH-1];
        first_length=FIRST_LENGTH-1;
        for(int i=0;i<first_length;i++){
            first[i]=FIRST_BYTE[i+1];
        }

        byte[] by1=endFlag.getBytes();
        END_BYTE=new int[by1.length];
        for(int i=0;i<by1.length;i++){
            END_BYTE[i]=by1[i];
        }
        END_LENGTH=END_BYTE.length;
        end=new int[END_LENGTH-1];
        end_length=END_LENGTH-1;
        for(int i=0;i<end_length;i++){
            end[i]=END_BYTE[i+1];
        }
    }
    @Override
    public void run() {
        init();

        try {
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

            int r = -1;
            Boolean isFindFirst = false;
            List<Byte>  temp = new LinkedList<Byte>();
            int[] endTemp = new int[end_length];
            while ((r = in.read()) != -1) {
                if (r == FIRST_BYTE[0]&&(!isFindFirst)) {
                    timeA=System.currentTimeMillis();
                    for (int i = 0; i < first_length; i++) {
                        r = in.read();
                        if (first[i] != r) {
                            break;
                        } else {
                            if (i ==(first_length-1))
                                isFindFirst = true;
                        }
                    }

                } else if (isFindFirst) {
                    temp.add((byte) r);
                    while ((r = in.read()) != -1) {
                        if (r == END_BYTE[0]) {
                            for (int i = 0; i < end_length; i++) {
                                r = in.read();
                                endTemp[i] = r;
                            }
                            if (endTemp[0] == end[0] && endTemp[1] == end[1] && endTemp[2] == end[2]&& endTemp[3] ==end[3]) {
                                whatNeedToDO(temp,in);
                                temp.clear();
                                isFindFirst=false;
                                break;
                            } else {
                                temp.add((byte)END_BYTE[0]);
                                for (int i = 0; i < endTemp.length; i++) {
                                    temp.add((byte) endTemp[i]);
                                }
                            }
                        } else {
                            temp.add((byte) r);
                        }
                    }
                }
            }
            //System.out.println(new String((new BASE64Decoder()).decodeBuffer(new String(st)), "utf-8"));
            socket.close();
        } catch (IOException ex) {
            System.out.println("连接可能已经断开，此连接中断" + socket.getPort());

        } finally {
            System.out.println("连接断开");
        }
    }

    private void whatNeedToDO(List<Byte> data,BufferedInputStream in) throws IOException {
        List<Byte> list=new LinkedList();
        list.addAll(data);
        int size=list.size();
        byte[] by=new byte[size];
        Iterator<Byte> it=list.iterator();
        int i=0;
        while (it.hasNext()){
            by[i]=it.next();
            i++;
        }
        byte[] s=Base64.decodeBase64(by);
        String ss=new String(s);
        JSONObject headJson=JSON.parseObject(ss);
        int leng=headJson.getInteger("dataSize");
        byte[] da=new byte[leng];
        in.read(da,0,leng);
        byte[] overCut=new byte[overFlag.length()];
        in.read(overCut,0,overFlag.length());
        Temp temp=new Temp();
        temp.setSocket(socket);
        JSONObject returnJson=null;
        if (Arrays.equals(overFlag.getBytes(),overCut)){
            temp.setCachedThreadPool(context.getCachedThreadPool());
            context.getCachedThreadPool().execute(new SocketTemp(temp));
            Ctrl ctrl=new Ctrl(headJson,da,temp);
            returnJson=ctrl.doSome();
            SendSocket sendSocket=new SendSocket(temp.getSocket(),returnJson);
            sendSocket.send();

        }else{
            returnJson.put("status",true);
            returnJson.put("msg","数据不完整");
            System.out.println("此条socket数据不是完整的数据");
            SendSocket sendSocket=new SendSocket(temp.getSocket(),returnJson);
            sendSocket.send();
        }
    }

}

