package oblachko.client;

import oblachko.util.Util;

import javax.swing.*;


public class ClientGUI extends JFrame {
    public ClientGUI(){
        setSize(Util.SCREEN_DIMENSION.width/2, Util.SCREEN_DIMENSION.height/2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Oblachko");
        setVisible(true);
    }
}
