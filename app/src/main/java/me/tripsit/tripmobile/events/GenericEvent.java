package me.tripsit.tripmobile.events;

public class GenericEvent {
    public static final int GET_FULL_CHAT = 1;

    public final int command;

    public GenericEvent(int command) {
        this.command = command;
    }
}
