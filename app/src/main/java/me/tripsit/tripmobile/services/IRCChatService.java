package me.tripsit.tripmobile.services;

import android.app.IntentService;
import android.content.Intent;

public class IRCChatService extends IntentService {
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
    }
}