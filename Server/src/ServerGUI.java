import javax.swing.*;

public class ServerGUI extends JFrame{
    public ServerGUI(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(Util.SCREEN_DIMENSION.width/2,Util.SCREEN_DIMENSION.height/2);
        setTitle("OblachkoServer");
        setVisible(true);
    }
}
