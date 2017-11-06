package oblachko.server.handlers;

import oblachko.library.messages.AuthMessage;
import oblachko.network.Message;
import oblachko.server.network.OblachkoSocketThread;

public class ServiceMessageHandler extends MessageHandler {
    @Override
    public void handleNonAuthorizedClient(OblachkoSocketThread client, Message message) {
        String login;
        String password;
        if(message instanceof AuthMessage){
            login = ((AuthMessage) message).getLogin();
            password = ((AuthMessage) message).getPassword();
            if(!checkCredentials(login,password)){
                client.authError();
            }else{
                client.authorizeAccept(login,null);
            }
        }
    }

    private boolean checkCredentials(String login, String password){
        if(login.equals("roma")&&password.equals("12345")) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void handleAuthorizedClient(OblachkoSocketThread client, Message message) {

    }
}
