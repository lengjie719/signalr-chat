package net.pingfang.signalr.chat.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import net.pingfang.signalr.chat.util.GlobalApplication;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.LogLevel;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;

/**
 * Created by gongguopei87@gmail.com on 2015/8/13.
 */
public class ChatService {

    public static final String URL = "http://192.168.0.254/signalr/hubs/";
    HubConnection connection;
    HubProxy hub;
    SignalRFuture<Void> awaitConnection;

    DemonThread thread;

    private Context context;

    private static ChatService chatService;

    private ChatService(Context context) {
        this.context = context;

        initConnection();
    }

    public static ChatService newInstance(Context context) {
        if(chatService == null) {
            chatService = new ChatService(context);
        }
        return chatService;
    }

    private void initConnection() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        connection = new HubConnection(URL, null, true, new Logger() {
            @Override
            public void log(String s, LogLevel logLevel) {

            }
        });

        awaitConnection = connection.start();

        hub = connection.createHubProxy("communicationHub");
        hub.on("broadcastMessage",
                new SubscriptionHandler2<String,String>() {
                    @Override
                    public void run(String msgType, String msg) {
                        Intent intent = new Intent();
                        intent.setAction(GlobalApplication.ACTION_INTENT_TEXT_MESSAGE_INCOMING);
                        intent.putExtra("name","server");
                        intent.putExtra("body", msg);
                        context.sendBroadcast(intent);
                    }
                },
                String.class,String.class);

        thread = new DemonThread();
        thread.start();
    }

    public void destroy() {
        if(thread != null) {
            thread.cancel();
            thread = null;
        }

        if(connection != null && connection.getState() == ConnectionState.Connected) {
            connection.stop();
        }
    }

    public void sendMessage(String buddy,String message) {
        new MessageSendTask().execute(buddy,message);
    }

    private class MessageSendTask extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {
            hub.invoke("send","directory",params[1]);
            return "ok";
        }
    }

    private class DemonThread extends Thread {

        boolean isRunning = true;

        @Override
        public void run() {
            while(isRunning) {
                try {
                    awaitConnection.get();
                } catch (InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
            }

            awaitConnection.cancel();
        }

        public void cancel() {
            if(isRunning) {
                isRunning = false;
            }
        }
    }

}
