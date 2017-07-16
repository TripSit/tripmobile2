package me.tripsit.tripmobile.events;

public class GenericEvent {
    public static final int GET_FULL_CHAT = 1;
    public static final int CHAT_READY = 2;

    public final int command;

    public GenericEvent(int command) {
        this.command = command;
    }
}
