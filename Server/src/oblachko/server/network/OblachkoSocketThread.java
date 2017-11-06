package oblachko.server.network;

import oblachko.network.SocketThread;
import oblachko.network.SocketThreadListener;

import java.net.Socket;
import java.util.Vector;

public class OblachkoSocketThread extends SocketThread {
    private boolean isAuthorized;
    private String nick;

    public OblachkoSocketThread(SocketThreadListener eventListener, String name, Socket socket) {
        super(eventListener, name, socket);
    }

    boolean isAuthorized(){
        return isAuthorized;
    }

    public String getNick() {
        return nick;
    }

    public void authError(){
        close();
    }

    public void authorizeAccept(String nick, Vector<String> clients){
        this.isAuthorized = true;
        this.nick = nick;
    }

    public void messageFormatError(String message){
        close();
    }
}
