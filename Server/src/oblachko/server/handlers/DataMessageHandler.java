package oblachko.server.handlers;

import oblachko.network.Message;
import oblachko.server.network.OblachkoSocketThread;

public class DataMessageHandler extends MessageHandler {
    @Override
    public void handleNonAuthorizedClient(OblachkoSocketThread client, Message message) {

    }

    @Override
    public void handleAuthorizedClient(OblachkoSocketThread client, Message message) {

    }
}
