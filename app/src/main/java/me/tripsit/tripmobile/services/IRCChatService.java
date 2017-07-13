package me.tripsit.tripmobile.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.RecieveEvent;
import me.tripsit.tripmobile.events.SendEvent;

public class IRCChatService extends Service {
    public static final int MSG_SAY_HELLO = 1;
    public int count = 0;

    private int port = 6697;
    private String host = "irc.tripsit.me";

    private Thread clientThread;


    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecieveEvent event) {
        //Toast.makeText(getApplicationContext(), event.message + " " + count, Toast.LENGTH_SHORT).show();
        count++;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_icon)
                        .setContentTitle("Connected to TripSit")
                        .setContentText("Have a great time!");

        Notification notification = mBuilder.build();

        startForeground(5, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        IRCClientRunnable irccr = new IRCClientRunnable();
        Thread t = new Thread(irccr);
        t.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy(){
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class IRCClientRunnable implements Runnable {
        ArrayList<String> queue = new ArrayList<String>();
        private volatile boolean cancelled;

        // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
        @Subscribe(threadMode = ThreadMode.POSTING)
        public void onMessageEvent(SendEvent event) {
            queue.add(event.message);
        }

        @Override
        public void run() {
            int count = 0;
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_MORE_FAVORABLE);
            clientThread = Thread.currentThread();
            if(!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
            for(int i = 0; i < 100; i++){
                System.out.println(i);
            }
            try {
                SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(host, port);

                BufferedReader dIn = new BufferedReader(new InputStreamReader(new DataInputStream(sslsocket.getInputStream())));

                PrintWriter out = new PrintWriter( new BufferedWriter( new OutputStreamWriter(sslsocket.getOutputStream())),true);
                String line = "";
                while (!cancelled) {
                    line = dIn.readLine();

                    if (line != null) {
                        System.out.println("--------------------------------- Got: " + line);
                        String[] commandParts = line.split(" ");
                        switch(commandParts[0]){
                            case "PING":
                                out.print("PONG " + commandParts[1]);
                                out.flush();
                                break;
                        }
                        EventBus.getDefault().postSticky(new RecieveEvent(line));
                    }

                    if(queue.toArray().length > 0){
                        String command = queue.remove(0) + "\r\n";
                        System.out.print("--------------------------------- " + command);
                        out.print(command);
                        out.flush();
                    }
                }

            }catch(IOException e){
                //TODO: something
            }
        }
    }
}