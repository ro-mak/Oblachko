import java.net.Socket;

public interface SocketThreadListener {
    void onStartSocketThread(SocketThread thread);
    void onStopSocketThread(SocketThread thread);

    void onReadySocketThread(SocketThread thread, Socket socket);
    void onReceiveServiceMessage(SocketThread socketThread, Socket socket, ServiceMessage serviceMessage);
    void onReceiveDataMessage(SocketThread socketThread, Socket socket,DataMessage dataMessage);
    void onExceptionSocketThread(SocketThread thread, Socket socket, Exception e);
}
