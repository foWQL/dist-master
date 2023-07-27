package com.pansky.websocket.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * @author Fo
 * @date 2023/5/15 15:32
 */
public class MyWebSocketClient extends WebSocketClient {

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
// TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onOpen ------");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onClose ------");
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        System.out.println("------ MyWebSocket onError ------");
    }

    @Override
    public void onMessage(String serMsg)  {
        // TODO Auto-generated method stub
        System.out.println("-------- 接收到服务端数据： " + serMsg + "--------");
        if(serMsg.contains("100")){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.close();
        }
    }



}
