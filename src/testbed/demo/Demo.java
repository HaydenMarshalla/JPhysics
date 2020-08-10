package testbed.demo;

import library.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Demo extends JPanel {
    private int width;
    private int height;
    private boolean antiAliasing;
    private World world;

    public Demo(int width, int height, boolean antiAliasing) {
        this.width = width;
        this.height = height;
        this.antiAliasing = antiAliasing;
        this.world = new World();
    }

    public static void showWindow(Demo gameScreen, String title) {
        if (gameScreen != null) {
            JFrame window = new JFrame(title);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(gameScreen);
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(gameScreen.width, gameScreen.height));
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);

            //gameScreen.start();
        }
    }
}

class KeyboardInput implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

class MouseInput implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}