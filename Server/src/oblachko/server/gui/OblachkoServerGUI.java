package oblachko.server.gui;

import oblachko.server.network.OblachkoServer;
import oblachko.server.network.OblachkoServerListener;
import oblachko.server.security.SQLSecurityManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class OblachkoServerGUI extends JFrame implements ActionListener, OblachkoServerListener,Thread.UncaughtExceptionHandler{

    private final String TITLE = "oblachko.server.network.OblachkoServer";
    private final String START_LISTENING = "Start listening";
    private final String STOP_LISTENING = "Stop listening";
    private final String DROP_ALL_CLIENTS = "Drop all clients";

    private final int WIDTH  = 800;
    private final int HEIGHT = 400;
    private final int positionX = 400;
    private final int positionY = 400;

    private final OblachkoServer oblachkoServer = new OblachkoServer(this,  new SQLSecurityManager());
    private final JButton buttonStartListening = new JButton(START_LISTENING);
    private final JButton buttonStopListening = new JButton(STOP_LISTENING);
    private final JButton buttonDropAllClients = new JButton(DROP_ALL_CLIENTS);
    private final JTextArea log = new JTextArea();


    public OblachkoServerGUI(){
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(positionX,positionY,WIDTH,HEIGHT);
        setTitle(TITLE);

        buttonStartListening.addActionListener(this);
        buttonStopListening.addActionListener(this);
        buttonDropAllClients.addActionListener(this);

        JPanel upperPanel = new JPanel(new GridLayout());
        upperPanel.add(buttonStartListening);
        upperPanel.add(buttonDropAllClients);
        upperPanel.add(buttonStopListening);
        add(upperPanel, BorderLayout.NORTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog,BorderLayout.CENTER);

        setVisible(true);
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if(source == buttonStartListening){
            oblachkoServer.startListening(8189);
        }else if(source == buttonDropAllClients){
            oblachkoServer.dropAllClients();
        }else if(source == buttonStopListening){
            oblachkoServer.stopListening();
        }else{
            throw new RuntimeException("Unknown source =" + source);
        }
    }

    @Override
    public void onOblachkoServerLog(OblachkoServer oblachkoServer, String message) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(message + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        String message;
        if(stackTraceElements.length == 0){
            message = "StackTraceElements has no elements";
        }else{
            message = e.getClass().getCanonicalName() + ": " + e.getMessage() +"\n"+stackTraceElements[0];
        }
        JOptionPane.showMessageDialog(null,message,"Exception: ",JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new OblachkoServerGUI();
            }
        });
    }
}

