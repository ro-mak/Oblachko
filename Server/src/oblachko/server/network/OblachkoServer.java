package oblachko.server.network;

import oblachko.network.*;
import oblachko.server.handlers.DataMessageHandler;
import oblachko.server.handlers.ServiceMessageHandler;
import oblachko.server.security.SecurityManager;

import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class OblachkoServer implements ServerSocketThreadListener, SocketThreadListener {
    private final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss ");
    private OblachkoServerListener oblachkoServerListener;
    private final SecurityManager securityManager;
    private ServerSocketThread serverSocketThread;
    private final Vector<SocketThread> clients = new Vector<>();
    private ServiceMessageHandler serviceMessageHandler;
    private DataMessageHandler dataMessageHandler;

    public OblachkoServer(OblachkoServerListener oblachkoServerListener, SecurityManager securityManager) {
        this.oblachkoServerListener = oblachkoServerListener;
        this.securityManager = securityManager;
        this.serviceMessageHandler = new ServiceMessageHandler();
        this.dataMessageHandler = new DataMessageHandler();
    }

    public void startListening(int port) {
        if (serverSocketThread != null && serverSocketThread.isAlive()) {
            putLog("oblachko.server.network.OblachkoServer is already launched");
            return;
        }
        serverSocketThread = new ServerSocketThread("oblachko.network.ServerSocketThread", port, 2000, this);
        putLog("oblachko.server.network.OblachkoServer has been launched.");
        securityManager.init();
    }

    public void dropAllClients() {
        putLog("drop all clients");
    }

    public void stopListening() {
        if (serverSocketThread == null || !serverSocketThread.isAlive()) {
            putLog("oblachko.server.network.OblachkoServer is not alive");
            return;
        }
        serverSocketThread.interrupt();
        securityManager.dispose();
    }


    //oblachko.network.ServerSocketThread
    @Override
    public void onStartServerSocketThread(ServerSocketThread thread) {
        putLog("started...");
    }

    @Override
    public void onStopServerSocketThread(ServerSocketThread thread) {
        putLog("stopped.");
    }

    @Override
    public void onReadyServerSocketThread(ServerSocketThread thread, ServerSocket serverSocket) {
        putLog("ServerSocket is ready");
    }

    @Override
    public void onAcceptedSocket(ServerSocketThread thread, ServerSocket serverSocket, Socket socket) {
        putLog("Client connected: " + socket);
        String threadName = "Socket thread: " + socket.getInetAddress() + ":" + socket.getPort();
        new OblachkoSocketThread(this, threadName, socket);
    }

    @Override
    public void onTimeOutAccept(ServerSocketThread thread, ServerSocket serverSocket) {
        //putLog("Timeout");
    }

    @Override
    public void onExceptionServerSocketThread(ServerSocketThread thread, Exception e) {
        putLog("Exception happened " + e.getClass().getName() + ": " + e.getMessage());
    }

    private synchronized void putLog(String message) {
        String messageLog = dateFormat.format(System.currentTimeMillis()) + ": " + message;
        if (oblachkoServerListener != null) oblachkoServerListener.onOblachkoServerLog(this, messageLog);

    }

    //oblachko.network.SocketThread
    @Override
    public synchronized void onStartSocketThread(SocketThread thread) {
        putLog("oblachko.network.SocketThread started...");
    }

    @Override
    public synchronized void onStopSocketThread(SocketThread thread) {
        clients.remove(thread);
        putLog("Socket thread stopped");
    }

    @Override
    public synchronized void onReadySocketThread(SocketThread thread, Socket socket) {
        putLog("Socket is Ready");
        clients.add(thread);
    }

    @Override
    public void onReceiveServiceMessage(SocketThread socketThread, Socket socket, ServiceMessage serviceMessage) {
        OblachkoSocketThread client = (OblachkoSocketThread) socketThread;
        if (client.isAuthorized()) {
            serviceMessageHandler.handleAuthorizedClient(client, serviceMessage);
        } else {
            serviceMessageHandler.handleNonAuthorizedClient(client, serviceMessage);
        }
    }

    @Override
    public void onReceiveDataMessage(SocketThread socketThread, Socket socket, DataMessage dataMessage) {
        OblachkoSocketThread client = (OblachkoSocketThread) socketThread;
        if (client.isAuthorized()) {
            dataMessageHandler.handleAuthorizedClient(client, dataMessage);
        } else {
            dataMessageHandler.handleNonAuthorizedClient(client, dataMessage);
        }
    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread thread, Socket socket, Exception e) {
        putLog("Exception happened " + e.getClass().getName() + ": " + e.getMessage());
        e.printStackTrace();
    }

}
