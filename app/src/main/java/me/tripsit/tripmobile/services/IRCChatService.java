package me.tripsit.tripmobile.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.GenericEvent;
import me.tripsit.tripmobile.events.MessageArrayEvent;
import me.tripsit.tripmobile.events.MessageEvent;
import me.tripsit.tripmobile.events.ReceiveEvent;
import me.tripsit.tripmobile.events.SendEvent;

public class IRCChatService extends Service {
    public boolean started = false;
    public static final int MSG_SAY_HELLO = 1;

    private int port = 6697;
    private String host = "irc.tripsit.me";

    ArrayList<MessageEvent> msgList = new ArrayList<MessageEvent>();
    int maxMsg = 300; //TODO: make adjustable

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReceiveEvent event) {
        if(msgList.size() >= maxMsg){
            msgList.remove(0);
        }
        MessageEvent me = new MessageEvent(event.user, event.message);
        msgList.add(me);

        System.out.println("-----------------" + event.message);
        EventBus.getDefault().postSticky(me);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GenericEvent event) {
        switch(event.command){
            case GenericEvent.GET_FULL_CHAT:
                MessageEvent[] sentArray = new MessageEvent[msgList.size()];
                sentArray = msgList.toArray(sentArray);
                EventBus.getDefault().postSticky(new MessageArrayEvent(sentArray));
                break;
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        try {
            Thread t = new Thread(new MyListener());
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            User user = event.getUser();
            String msg = event.getMessage();
            if (msg.startsWith("?helloworld")) {
                event.respond("Hello world!");
            }
            EventBus.getDefault().postSticky(new ReceiveEvent(user, msg));
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