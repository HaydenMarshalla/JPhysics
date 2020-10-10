package testbed.demo;

import library.dynamics.Body;
import library.dynamics.Raycast;
import library.dynamics.World;
import library.dynamics.RaycastScatter;
import library.explosions.ProximityExplosion;
import library.utils.ColourSettings;
import library.joints.Joint;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.Trail;
import testbed.demo.input.KeyBoardInput;
import testbed.demo.input.MouseInput;
import testbed.demo.input.MouseScroll;

import testbed.demo.tests.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

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

        BouncingBall.load(this);
        physicsThread.start();
    }

    public ArrayList<Raycast> rays = new ArrayList<>();

    public void add(Raycast r) {
        rays.add(r);
    }

    public ArrayList<RaycastScatter> scatterRays = new ArrayList<>();

    public void add(RaycastScatter r) {
        scatterRays.add(r);
    }

    public ArrayList<ProximityExplosion> proximityPoints = new ArrayList<>();

    public void add(ProximityExplosion r) {
        proximityPoints.add(r);
    }

    private World world = new World();

    public void createWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    private boolean running = true;

    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    @Override
    public void run() {
        while (running) {
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException e) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            world.step();
            updateTrails();
            for (ProximityExplosion p : proximityPoints) {
                p.updateProximity(world.bodies);
            }
            for (Raycast r : rays) {
                r.updateProjection(world.bodies);
            }
            for (RaycastScatter r : scatterRays) {
                r.updateRays();
            }
            repaint();
        }
    }

    public void stop() {
        running = false;
        resume();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public ArrayList<Trail> trailsToBodies = new ArrayList<>();

    public void add(Trail r) {
        trailsToBodies.add(r);
    }

    private void updateTrails() {
        for (Trail t : trailsToBodies) {
            t.updateTrail();
        }
    }

    private ColourSettings paintSettings = new ColourSettings();

    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = false;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = false;
    private boolean drawGrid = true;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (antiAliasing) g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (world != null) {
            if (drawGrid) {
                drawGridMethod(g2d);
            }
            drawTrails(g2d);
            for (Body b : world.bodies) {
                if (drawShapes) {
                    b.shape.draw(g2d, paintSettings, camera);
                }
                if (drawAABBs) {
                    b.shape.drawAABB(g2d, paintSettings, camera);
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
                    b.shape.drawCOMS(g2d, paintSettings, camera);
                }
            }
            if (drawJoints) {
                for (Joint j : world.joints) {
                    j.draw(g2d, camera);
                }
            }
            for (ProximityExplosion p : proximityPoints) {
                p.draw(g2d, paintSettings, camera);
            }
            for (Raycast r : rays) {
                r.draw(g2d, paintSettings, camera);
            }
            for (RaycastScatter r : scatterRays) {
                r.draw(g2d, paintSettings, camera);
            }
        }
    }

    private void drawGridMethod(Graphics2D g2d) {
        int projection = 20000;
        int spacing = 10;
        int minXY = -projection;
        int maxXY = projection;
        int totalProjectionDistance = projection + projection;
        g2d.setColor(paintSettings.gridLines);
        for (int i = 0; i <= totalProjectionDistance; i += spacing) {
            if (i == projection) {
                g2d.setStroke(paintSettings.axisStrokeWidth);
                g2d.setColor(paintSettings.gridAxis);
            }

            Vectors2D currentMinY = camera.scaleToScreen(new Vectors2D(minXY + i, minXY));
            Vectors2D currentMaxY = camera.scaleToScreen(new Vectors2D(minXY + i, maxXY));
            g2d.draw(new Line2D.Double(currentMinY.x, currentMinY.y, currentMaxY.x, currentMaxY.y));

            Vectors2D currentMinX = camera.scaleToScreen(new Vectors2D(minXY, minXY + i));
            Vectors2D currentMaxX = camera.scaleToScreen(new Vectors2D(maxXY, minXY + i));
            g2d.draw(new Line2D.Double(currentMinX.x, currentMinX.y, currentMaxX.x, currentMaxX.y));

            if (i == projection) {
                g2d.setStroke(paintSettings.defaultStrokeWidth);
                g2d.setColor(paintSettings.gridLines);
            }
        }
    }

    private void drawTrails(Graphics2D g) {
        g.setColor(paintSettings.trail);
        for (Trail t : trailsToBodies) {
            Path2D.Double s = new Path2D.Double();
            for (int i = 0; i < t.getTrailPoints().length; i++) {
                Vectors2D v = t.getTrailPoints()[i];
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

    public void setCamera(Vectors2D centre, double zoom) {
        camera.setCentre(centre);
        camera.setZoom(zoom);
    }

    public Camera getCamera() {
        return camera;
    }
}