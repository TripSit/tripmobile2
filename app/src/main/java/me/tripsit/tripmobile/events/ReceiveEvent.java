package me.tripsit.tripmobile.events;

import org.pircbotx.User;

public class ReceiveEvent {
    public final User user;
    public final String message;

    public ReceiveEvent(User user, String message) {
        this.user = user;
        this.message = message;
    }
}
