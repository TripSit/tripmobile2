package me.tripsit.tripmobile.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.MessageEvent;

public class IRCChatService extends Service {
    public static final int MSG_SAY_HELLO = 1;
    public int count = 0;

    private Socket socket;

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Toast.makeText(getApplicationContext(), event.message + " " + count, Toast.LENGTH_SHORT).show();
        count++;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");

        Notification notification = mBuilder.build();

        startForeground(5, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
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
}