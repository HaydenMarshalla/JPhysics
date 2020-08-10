package testbed.demo;

import library.Body;
import library.Circle;
import library.World;
import library.math.Vectors2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class demoWindow extends JPanel {
    private int width;
    private int height;
    private boolean antiAliasing;
    private World world;

    public demoWindow(int width, int height, boolean antiAliasing) {
        this.width = width;
        this.height = height;
        this.antiAliasing = antiAliasing;
        this.world = new World(new Vectors2D(0,-1));
        world.addBody(new Body(new Circle(50), 100,100));
        world.addBody(new Body(new Circle(50), 201,100));
    }

    public static void showWindow(demoWindow gameScreen, String title) {
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

    private boolean drawAABB = false;
    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = false;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gi = (Graphics2D) g;
        if (antiAliasing) gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (world != null) {
            world.step(0.00001,10);
            if (drawShapes) {
                for (Body b : world.bodies) {
                    b.shape.draw(g);
                }
            }
            repaint();
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