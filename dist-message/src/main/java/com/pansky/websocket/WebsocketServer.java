package com.pansky.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.text.NumberFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//import net.sf.json.JSONObject;

/**
 * @author Fo
 * @date 2023/5/15 15:09
 * @Describe @ServerEndpoint注解表示websocket请求url，{oid}用来标识每一个websocket客户端的id。
 */


@Component
@ServerEndpoint(value = "/websocket/{oid}")
public class WebsocketServer {

    private Session session;
    private String oid;
    private static Map<String, Session> clients = new ConcurrentHashMap<>();



    /**
     * * 连接建立后触发的方法
     */
    @OnOpen
    public void onOpen(@PathParam("oid") String oid, Session session) {
        this.session = session;
        this.oid = oid;
        clients.put(oid, session);
        System.out.println(oid + "已连接");
    }
    /**
     * * 连接关闭后触发的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        clients.remove(this.oid);
        System.out.println("关闭连接:" + this.oid + " ======");
    }
    /**
     * * 接收到客户端消息时触发的方法
     */
    @OnMessage
    public void onMessage(String params, Session session) throws Exception {
        System.out.println("服务端：收到来自" + this.oid + "的消息" + params);
        String result = "收到来自" + this.oid + "的消息" + params;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        for (int i = 0; i <= 100; i++) {
            if (i%10==0) {
                String process = numberFormat.format((double) i / 100.00*100);
                session.getBasicRemote().sendText(result+"：流转进度"+process+"%");
                Thread.sleep(1000);
            }

        }
        //返回消息给Web Socket客户端（浏览器）
        /** session.getAsyncRemote()表示异步发送
            session.getBasicRemote()表示同步发送
         **/
//        session.getBasicRemote().sendText(result);
    }
    /**
     * * 发生错误时触发的方法
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println(session.getId() + "连接发生错误" + error.getMessage());
        error.printStackTrace();
    }


    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }
}

