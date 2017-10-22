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

    public OblachkoServer(OblachkoServerListener oblachkoServerListener,SecurityManager securityManager){
        this.oblachkoServerListener = oblachkoServerListener;
        this.securityManager = securityManager;
    }

    public void startListening(int port){
        if(serverSocketThread != null && serverSocketThread.isAlive()) {
            putLog("OblachkoServer is already launched");
            return;
        }
        serverSocketThread = new ServerSocketThread("ServerSocketThread",port,2000,this);
        putLog("OblachkoServer has been launched.");
        securityManager.init();
    }

    public void dropAllClients(){
        putLog("drop all clients");
    }

    public void stopListening()
    {
        if(serverSocketThread == null || !serverSocketThread.isAlive()){
            putLog("OblachkoServer is not alive");
            return;
        }
        serverSocketThread.interrupt();
        securityManager.dispose();
    }



    //ServerSocketThread
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
        new OblachkoSocketThread(this,threadName,socket);
    }

    @Override
    public void onTimeOutAccept(ServerSocketThread thread, ServerSocket serverSocket) {
        //putLog("Timeout");
    }

    @Override
    public void onExceptionServerSocketThread(ServerSocketThread thread, Exception e) {
        putLog("Exception happened " + e.getClass().getName() + ": " + e.getMessage());
    }

    private synchronized void putLog(String message){
        String messageLog = dateFormat.format(System.currentTimeMillis())+": " + message;
        if(oblachkoServerListener != null) oblachkoServerListener.onOblachkoServerLog(this,messageLog);

    }

    //SocketThread
    @Override
    public synchronized void onStartSocketThread(SocketThread thread) {
        putLog("SocketThread started...");
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

    }

    @Override
    public void onReceiveDataMessage(SocketThread socketThread, Socket socket, DataMessage dataMessage) {

    }


    private void handleNonAuthorizedClient(OblachkoSocketThread client, DataMessage message){

    }

    private void handleAuthorizedClient(OblachkoSocketThread client,DataMessage message){

    }

    @Override
    public synchronized void onExceptionSocketThread(SocketThread thread, Socket socket, Exception e) {
        putLog("Exception happened " + e.getClass().getName() + ": " + e.getMessage());
    }

}
