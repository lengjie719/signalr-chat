package net.pinggang.signalr.chat;

import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;

public class MainActivity extends AppCompatActivity {

    String host = "http://192.168.0.254";
    // Change to the IP address and matching port of your SignalR server.
    HubConnection connection;
    HubProxy hub;

    Handler handler;

    TextView tv_statusField;
    DemoConnection thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_statusField = (TextView) findViewById(R.id.tv_statusField);

        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        connection = new HubConnection( host );
        hub = connection.createHubProxy( "MessageHub" );
        hub.on("UpdateStatus",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(String status) {
                        // Since we are updating the UI,
                        // we need to use a handler of the UI thread.
                        final String fStatus = status;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tv_statusField.setText(fStatus);
                            }
                        });
                    }
                }
                , String.class);

        thread = new DemoConnection();
        thread.start();
    }

    public void submit(View view) {
        new SendMessageTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(thread != null) {
            thread.cancel();
        }

        if(connection != null) {
            connection.stop();
        }
    }

    private  class DemoConnection extends Thread {

        boolean flag = true;

        @Override
        public void run() {
            SignalRFuture<Void> awaitConnection = connection.start();
            while (flag) {
                try {
                    awaitConnection.get();
                } catch (InterruptedException e) {
                    // Handle ...
                } catch (ExecutionException e) {
                    // Handle ...
                }
            }

            awaitConnection.cancel();

        }

        public void cancel() {
            if(flag) {
                flag = false;
            }
        }

    }

    private class SendMessageTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                hub.invoke( "SendMessage", "Client", "Hello world!" ).get();
            } catch (InterruptedException e) {
                // Handle ...
                return "InterruptedException";
            } catch (ExecutionException e) {
                // Handle ...
                return "ExecutionException";
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            tv_statusField.setText("STATE == " + s);
        }
    }

}
