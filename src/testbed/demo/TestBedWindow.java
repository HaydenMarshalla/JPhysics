package testbed.demo;

import library.*;
import library.joints.Joint;
import testbed.demo.input.KeyBoardInput;
import testbed.demo.input.MouseInput;
import testbed.demo.input.MouseScroll;

import testbed.demo.tests.*;

import javax.swing.*;
import java.awt.*;

public class TestBedWindow extends JPanel implements Runnable {
    private int windowWidth;
    private int windowHeight;
    private boolean antiAliasing;

    private Thread physicsThread;
    private Camera camera;

    //Input handler classes
    KeyBoardInput keyInput;
    MouseInput mouseInput;
    MouseScroll mouseScrollInput;

    public TestBedWindow(int width, int height, boolean antiAliasing) {
        this.windowWidth = width;
        this.windowHeight = height;
        this.antiAliasing = antiAliasing;

        physicsThread = new Thread(this);

        camera = new Camera(1920, 1080, this);


        mouseInput = new MouseInput(this);
        addMouseListener(mouseInput);

        keyInput = new KeyBoardInput(camera);
        addKeyListener(keyInput);

        mouseScrollInput = new MouseScroll(camera);
        addMouseWheelListener(mouseScrollInput);

        startThread();
    }

    private World world = new World();

    public void startThread() {
        Restitution.load(this);
        running = true;
        physicsThread.start();
    }

    private boolean running = false;

    @Override
    public void run() {
        while (running) {
            world.step(0.000001, 10);
            repaint();
        }
    }

    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = true;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = false;

    private ColourSettings paintSettings = new ColourSettings();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gi = (Graphics2D) g;
        if (antiAliasing) gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (world != null) {
            for (Body b : world.bodies) {
                if (drawShapes) {
                    b.shape.draw(g, paintSettings, camera);
                }
                if (drawAABBs) {
                    b.shape.drawAABB(g, paintSettings, camera);
                }
                if (drawContactPoints) {
                    //TO DO
                }
                if (drawContactNormals) {
                    //TO DO
                }
                if (drawContactImpulse) {
                    //TO DO
                }
                if (drawFrictionImpulse) {
                    //TO DO
                }
                if (drawCOMs) {
                    //TO DO
                    b.shape.drawCOMS(g, paintSettings, camera);
                }
            }
            if (drawJoints) {
                for (Joint j : world.joints) {
                    j.draw(gi, camera);
                }
            }
        }
    }

    public static void showWindow(TestBedWindow gameScreen, String title) {
        if (gameScreen != null) {
            JFrame window = new JFrame(title);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(gameScreen);
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(gameScreen.windowWidth, gameScreen.windowHeight));
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            gameScreen.setFocusable(true);
            gameScreen.setOpaque(true);
            gameScreen.setBackground(gameScreen.paintSettings.background);
        }
    }

    public void createWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}