package me.tripsit.tripmobile.events;

public class MessageArrayEvent {
    public final MessageEvent[] array;

    public MessageArrayEvent(MessageEvent[] array) {
        this.array = array;
    }
}
