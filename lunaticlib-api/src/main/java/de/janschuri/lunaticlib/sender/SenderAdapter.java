package de.janschuri.lunaticlib.sender;

public interface SenderAdapter<P, T> {
    Sender getSender(T sender);
}
