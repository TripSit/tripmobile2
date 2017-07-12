package me.tripsit.tripmobile.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tripsit.tripmobile.MainActivity;
import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.services.IRCChatService;

public class ChatFragment extends Fragment {
    public void sayHello() {
        MainActivity activity = (MainActivity)getActivity();
        if (!activity.mBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain();
        msg.what = IRCChatService.MSG_SAY_HELLO;
        msg.obj = "test";
        try {
            activity.mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sayHello();
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }
}