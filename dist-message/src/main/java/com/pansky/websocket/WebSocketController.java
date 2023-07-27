package com.pansky.websocket;

import com.pansky.websocket.client.MyWebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author Fo
 * @date 2023/5/15 15:39
 */
@RestController
public class WebSocketController {



    public static void main(String[] args) {
        try {

            // 创建WebSocket客户端
            MyWebSocketClient myClient = new MyWebSocketClient(new URI("ws://127.0.0.1:9000/websocket/111"));
            // 与服务端建立连接
            myClient.connect();
            while (!myClient.getReadyState().equals(ReadyState.OPEN)) {
                System.out.println("连接中。。。");
                Thread.sleep(1000);
            }
            // 往websocket服务端发送数据
            myClient.send("发送来自websocketClient 111的消息");
            /*Thread.sleep(3000);
            // 关闭与服务端的连接
             myClient.close();*/
        }catch (Exception e){
            e.printStackTrace();
        }
        // write your code here

    }
}

