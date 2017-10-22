import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {

    private final SocketThreadListener eventListener;
    private final Socket socket;
    private ObjectOutputStream objectOutputStream;


    public SocketThread(SocketThreadListener eventListener, String name, Socket socket){
        super(name);
        this.eventListener = eventListener;
        this.socket = socket;
        start();
    }


    @Override
    public void run() {
        eventListener.onStartSocketThread(this);
        try{
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            eventListener.onReadySocketThread(this,socket);
            while (!isInterrupted()){
                Object message = objectInputStream.readObject();
                if(message instanceof DataMessage){
                    eventListener.onReceiveDataMessage(this,socket,(DataMessage) message);
                }else if(message instanceof ServiceMessage){
                    eventListener.onReceiveServiceMessage(this,socket,(ServiceMessage)message);
                }
            }
        }catch (IOException | ClassNotFoundException e){
            eventListener.onExceptionSocketThread(this,socket,e);
        }finally {
            try {
                socket.close();
            }catch (IOException e){
                eventListener.onExceptionSocketThread(this,socket,e);
            }
            eventListener.onStopSocketThread(this);
        }
    }

    public synchronized void sendMessage(String message){
        try {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            eventListener.onExceptionSocketThread(this,socket,e);
            close();
        }
    }

    public synchronized void close(){
        interrupt();
        try{
            socket.close();
        }catch (IOException e){
            eventListener.onExceptionSocketThread(this,socket,e);
        }
    }
}
