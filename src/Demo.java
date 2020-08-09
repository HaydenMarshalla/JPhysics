import javax.swing.*;
import java.awt.*;

public class Demo extends JPanel {
    private int width;
    private int height;
    private boolean antiAliasing;

    public Demo(int width, int height, boolean antiAliasing){
        this.width = width;
        this.height = height;
        this.antiAliasing = antiAliasing;

    }

    public static void showWindow( Demo gameScreen, String title )
    {
        if (gameScreen != null)
        {
            JFrame window = new JFrame( title );
            window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            window.add( gameScreen );
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(gameScreen.width, gameScreen.height));
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible( true );

            //gameScreen.start();
        }
    }
}
