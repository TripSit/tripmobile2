package me.tripsit.tripmobile.events;

import org.pircbotx.User;

public class MessageEvent {
    public final User user;
    public final String message;

    public MessageEvent(User user, String message) {
        this.user = user;
        this.message = message;
    }
}
