package me.tripsit.tripmobile.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

import me.tripsit.tripmobile.R;
import me.tripsit.tripmobile.events.RecieveEvent;
import me.tripsit.tripmobile.events.SendEvent;

public class ChatFragment extends ListFragment implements View.OnClickListener{

    ArrayAdapter<String> aa;
    ArrayList<String> listItems = new ArrayList<>();

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecieveEvent event) {
        listItems.add(event.message);
        aa.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        aa = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listItems);
        setListAdapter(aa);

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