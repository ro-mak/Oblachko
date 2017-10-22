package oblachko.server.handlers;


import oblachko.network.Message;
import oblachko.server.network.OblachkoSocketThread;

public abstract class MessageHandler {

    public abstract void handleNonAuthorizedClient(OblachkoSocketThread client, Message message);

    public abstract void handleAuthorizedClient(OblachkoSocketThread client, Message message);
}
