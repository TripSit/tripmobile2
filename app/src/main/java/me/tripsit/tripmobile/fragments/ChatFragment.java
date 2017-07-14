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

        Button submit = (Button) v.findViewById(R.id.button);
        submit.setOnClickListener(this);

        Button nick = (Button) v.findViewById(R.id.nick);
        nick.setOnClickListener(this);

        Button user = (Button) v.findViewById(R.id.user);
        user.setOnClickListener(this);

        Button join = (Button) v.findViewById(R.id.join);
        join.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        final TextView command = (TextView) getView().findViewById(R.id.editText);
        final Button submit = (Button) getView().findViewById(R.id.button);
        switch (v.getId()) {
            case R.id.button:
                EventBus.getDefault().postSticky(new SendEvent(command.getText() + ""));
                break;
            case R.id.nick:
                EventBus.getDefault().postSticky(new SendEvent("NICK TripAppTest"));
                break;
            case R.id.user:
                EventBus.getDefault().postSticky(new SendEvent("USER TripAppTest TripAppTest TripAppTest :Test app"));
                break;
            case R.id.join:
                EventBus.getDefault().postSticky(new SendEvent("JOIN ##luciditystill"));
                break;
        }
    }
}