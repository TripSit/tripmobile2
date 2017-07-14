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
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.RecieveEvent;
import me.tripsit.tripmobile.events.SendEvent;

public class IRCChatService extends Service {
    public static final int MSG_SAY_HELLO = 1;
    public int count = 0;

    private int port = 6697;
    private String host = "irc.tripsit.me";

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

        try {
            Thread t = new Thread(new MyListener());
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
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

    public class MyListener extends ListenerAdapter implements Runnable{
        PircBotX bot;

        @Subscribe(threadMode = ThreadMode.POSTING)
        public void onMessageEvent(SendEvent event) {
            bot.send().message("##luciditystill", event.message);
        }

        @Override
        public void onGenericMessage(GenericMessageEvent event) {
            event.getUser();
            String msg = event.getMessage();
            System.out.println("---------------------------------" + msg);
            EventBus.getDefault().postSticky(new RecieveEvent(msg));
            if (msg.startsWith("?helloworld"))
                event.respond("Hello world!");
        }

        public void run(){
            if(!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }

            ListenerAdapter myL = new MyListener();
            String randomID = java.util.UUID.randomUUID().toString().substring(0,6);
            Configuration configuration = new Configuration.Builder()
                    .setName("AppUser" + randomID)
                    .setRealName(randomID)
                    .setLogin(randomID)
                    .addServer("chat.tripsit.me")
                    .addAutoJoinChannel("##luciditystill")
                    .addListener(myL)
                    .buildConfiguration();

            bot = new PircBotX(configuration);
            try {
                bot.startBot();
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
        }
    }
}