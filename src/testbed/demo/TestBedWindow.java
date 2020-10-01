package testbed.demo;

import library.*;
import library.Timer;
import library.joints.Joint;
import library.math.Vectors2D;
import raycasts.RayCast;
import testbed.demo.input.KeyBoardInput;
import testbed.demo.input.MouseInput;
import testbed.demo.input.MouseScroll;

import testbed.demo.tests.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class TestBedWindow extends JPanel implements Runnable {
    private final boolean antiAliasing;

    private final Thread physicsThread;
    private final Camera camera;

    //Input handler classes
    KeyBoardInput keyInput;
    MouseInput mouseInput;
    MouseScroll mouseScrollInput;

    public TestBedWindow(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;

        physicsThread = new Thread(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        camera = new Camera((int) screenSize.getWidth(), (int) screenSize.getHeight(), this);

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
        BouncingObject.load(this);
        physicsThread.start();
    }

    private boolean running = true;

    @Override
    public void run() {
        int updates = 0;
        int frames = 0;
        long excessTime = 0L;
        long frameTime = (long) (1e+9 / Settings.FPS);
        Timer time = new Timer();
        while (running) {
            time.reset();
            while (time.timePassed() + excessTime < frameTime) {
                world.step();
                updates++;
            }
            for (Body b : world.bodies) {
                if (b.trailObj != null) {
                    b.trailObj.updateTrail(b);
                }
            }
            updates = 0;
            repaint();
            excessTime = time.timePassed() - frameTime;
        }
    }

    private ColourSettings paintSettings = new ColourSettings();

    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = true;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = true;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gi = (Graphics2D) g;
        if (antiAliasing) gi.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (world != null) {
         /*   //GRID CODE work in progress
            gi.setColor(Color.WHITE);
            int spacing = 50;
            int projection = 100000;
            int loop = projection;
            Vectors2D x = new Vectors2D(0, -projection);
            for (int i = 0; i < loop; i += spacing/2) {
                Vectors2D v = camera.scaleToScreen(new Vectors2D(projection, x.y));
                Vectors2D v2 = camera.scaleToScreen(new Vectors2D(-projection, x.y));
                x.add(new Vectors2D(0, spacing));
                gi.draw(new Line2D.Double(v.x, v.y, v2.x, v2.y));
            }

            Vectors2D y = new Vectors2D(-projection, 0);
            for (int i = 0; i < loop; i += spacing/2) {
                Vectors2D v = camera.scaleToScreen(new Vectors2D(y.x,projection));
                Vectors2D v2 = camera.scaleToScreen(new Vectors2D(y.x,-projection));
                y.add(new Vectors2D(spacing,0));
                gi.draw(new Line2D.Double(v.x, v.y, v2.x, v2.y));
            }*/

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
                drawTrails(gi, b);
            }
            if (drawJoints) {
                for (Joint j : world.joints) {
                    j.draw(gi, camera);
                }
            }
            for (RayCast r : world.raycastObjects) {
                r.draw(g, paintSettings, camera);
            }
        }
    }

    private void drawTrails(Graphics2D g, Body b) {
        if (b.trailObj != null) {
            g.setColor(paintSettings.trail);
            Path2D.Double s = new Path2D.Double();
            for (int i = 0; i < b.trailObj.getTrailPoints().length; i++) {
                Vectors2D v = b.trailObj.getTrailPoints()[i];
                if (v == null) {
                    break;
                } else {
                    v = camera.scaleToScreen(v);
                    if (i == 0) {
                        s.moveTo(v.x, v.y);
                    } else {
                        s.lineTo(v.x, v.y);
                    }
                }
            }
            g.draw(s);
        }
    }

    public static void showWindow(TestBedWindow gameScreen, String title, int windowWidth, int windowHeight) {
        if (gameScreen != null) {
            JFrame window = new JFrame(title);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(gameScreen);
            window.setMinimumSize(new Dimension(800, 600));
            window.setPreferredSize(new Dimension(windowWidth, windowHeight));
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

    public void setCamera(Vectors2D centre, double zoom) {
        camera.setCentre(centre);
        camera.setZoom(zoom);
    }

    public Camera getCamera() {
        return camera;
    }
}