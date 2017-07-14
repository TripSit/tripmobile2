package me.tripsit.tripmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.RecieveEvent;
import me.tripsit.tripmobile.events.SendEvent;

public class ChatFragment extends Fragment implements View.OnClickListener{
    /*
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        Button send = (Button) v.findViewById(R.id.sendButton);
        send.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final TextView message = (TextView) getView().findViewById(R.id.message);
        switch (v.getId()) {
            case R.id.sendButton:
                EventBus.getDefault().postSticky(new SendEvent(message.getText() + ""));
                message.setText("");
                break;
        }

    }
}