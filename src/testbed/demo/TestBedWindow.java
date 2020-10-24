package testbed.demo;

import library.dynamics.Body;
import library.dynamics.Ray;
import library.dynamics.World;
import library.explosions.ParticleExplosion;
import library.explosions.ProximityExplosion;
import library.joints.Joint;
import library.utils.ColourSettings;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.Trail;
import testbed.demo.input.*;
import testbed.demo.tests.ProximityExplosionTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class TestBedWindow extends JPanel implements Runnable {
    private final Camera CAMERA;

    public void setCamera(Vectors2D centre, double zoom) {
        CAMERA.setCentre(centre);
        CAMERA.setZoom(zoom);
    }

    public Camera getCamera() {
        return CAMERA;
    }

    private final boolean ANTIALIASING;
    private final Thread PHYSICS_THREAD;

    //Input handler classes
    private final KeyBoardInput KEY_INPUT;
    private final MouseInput MOUSE_INPUT;
    private final MouseScroll MOUSE_SCROLL_INPUT;
    private final MouseMotionListener MOUSE_MOTION_INPUT;

    public TestBedWindow(boolean antiAliasing) {
        this.ANTIALIASING = antiAliasing;

        PHYSICS_THREAD = new Thread(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        CAMERA = new Camera((int) screenSize.getWidth(), (int) screenSize.getHeight(), this);

        MOUSE_INPUT = new MouseInput(this);
        addMouseListener(MOUSE_INPUT);

        KEY_INPUT = new KeyBoardInput(this);
        addKeyListener(KEY_INPUT);

        MOUSE_SCROLL_INPUT = new MouseScroll(this);
        addMouseWheelListener(MOUSE_SCROLL_INPUT);

        MOUSE_MOTION_INPUT = new MouseMovement(this);
        addMouseMotionListener(MOUSE_MOTION_INPUT);

        ProximityExplosionTest.load(this);
    }

    public void startThread() {
        PHYSICS_THREAD.start();
    }

    public ArrayList<Ray> rays = new ArrayList<>();

    public void add(Ray ray) {
        rays.add(ray);
    }

    public ArrayList<ProximityExplosion> getProximityExp() {
        return proximityExp;
    }

    public ArrayList<ProximityExplosion> proximityExp = new ArrayList<>();

    public void add(ProximityExplosion ex) {
        proximityExp.add(ex);
    }

    public ArrayList<ParticleExplosion> particles = new ArrayList<>();

    public void add(ParticleExplosion p) {
        particles.add(p);
        for (Body b : p.getParticles()) {
            trailsToBodies.add(new Trail(1000, 100, b));
        }
    }

    private World world = new World();

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    public ArrayList<Trail> trailsToBodies = new ArrayList<>();

    public void add(Trail trail) {
        trailsToBodies.add(trail);
    }

    private boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public void stop() {
        running = false;
        PHYSICS_THREAD.interrupt();
    }

    public void pause() {
        paused = true;
    }

    public boolean isPaused() {
        return paused;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    private void updateProximityCast() {
        for (ProximityExplosion p : proximityExp) {
            p.updateProximity(world.bodies);
        }
    }

    private void updateRays() {
        for (Ray r : rays) {
            r.updateProjection(world.bodies);
        }
    }

    private void updateTrails() {
        for (Trail t : trailsToBodies) {
            t.updateTrail();
        }
    }

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
            try {
                world.step();
                updateTrails();
                updateProximityCast();
                updateRays();
                repaint();
            } catch (ConcurrentModificationException e) {
            }
        }
    }

    public void clearTestbedObjects() {
        CAMERA.reset();
        world.clearWorld();
        trailsToBodies.clear();
        rays.clear();
        proximityExp.clear();
        repaint();
    }

    private ColourSettings paintSettings = new ColourSettings();

    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = false;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = true;
    private boolean drawGrid = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (ANTIALIASING) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if (world != null) {
            if (drawGrid) {
                drawGridMethod(g2d);
            }
            drawTrails(g2d);
            for (Body b : world.bodies) {
                if (drawShapes) {
                    b.shape.draw(g2d, paintSettings, CAMERA);
                }
                if (drawAABBs) {
                    b.shape.drawAABB(g2d, paintSettings, CAMERA);
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
                    b.shape.drawCOMS(g2d, paintSettings, CAMERA);
                }
            }
            if (drawJoints) {
                for (Joint j : world.joints) {
                    j.draw(g2d, paintSettings, CAMERA);
                }
            }
            for (ProximityExplosion p : proximityExp) {
                p.draw(g2d, paintSettings, CAMERA);
            }
            for (Ray r : rays) {
                r.draw(g2d, paintSettings, CAMERA);
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

            Vectors2D currentMinY = CAMERA.convertToScreen(new Vectors2D(minXY + i, minXY));
            Vectors2D currentMaxY = CAMERA.convertToScreen(new Vectors2D(minXY + i, maxXY));
            g2d.draw(new Line2D.Double(currentMinY.x, currentMinY.y, currentMaxY.x, currentMaxY.y));

            Vectors2D currentMinX = CAMERA.convertToScreen(new Vectors2D(minXY, minXY + i));
            Vectors2D currentMaxX = CAMERA.convertToScreen(new Vectors2D(maxXY, minXY + i));
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
                    v = CAMERA.convertToScreen(v);
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
            gameScreen.setFocusable(true);
            gameScreen.setOpaque(true);
            gameScreen.setBackground(gameScreen.paintSettings.background);

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("Demos");
            menu.setMnemonic(KeyEvent.VK_M);
            menuBar.add(menu);
            window.setJMenuBar(menuBar);

            JMenuItem bouncingBall = new JMenuItem("Bouncing ball");
            bouncingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK));
            menu.add(bouncingBall);
            bouncingBall.addActionListener(new MenuInput(gameScreen));

            JMenuItem car = new JMenuItem("Car");
            car.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
            menu.add(car);
            car.addActionListener(new MenuInput(gameScreen));

            JMenuItem chains = new JMenuItem("Chains");
            chains.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK));
            menu.add(chains);
            chains.addActionListener(new MenuInput(gameScreen));

            JMenuItem compoundBodies = new JMenuItem("Compound bodies");
            compoundBodies.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK));
            menu.add(compoundBodies);
            compoundBodies.addActionListener(new MenuInput(gameScreen));

            JMenuItem drag = new JMenuItem("Drag");
            drag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK));
            menu.add(drag);
            drag.addActionListener(new MenuInput(gameScreen));

            JMenuItem friction = new JMenuItem("Friction");
            friction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK));
            menu.add(friction);
            friction.addActionListener(new MenuInput(gameScreen));

            JMenuItem lineOfSight = new JMenuItem("Line of sight");
            lineOfSight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK));
            menu.add(lineOfSight);
            lineOfSight.addActionListener(new MenuInput(gameScreen));

            JMenuItem mixedShapes = new JMenuItem("Mixed shapes");
            mixedShapes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK));
            menu.add(mixedShapes);
            mixedShapes.addActionListener(new MenuInput(gameScreen));

            JMenuItem newtonsCradle = new JMenuItem("Newtons cradle");
            newtonsCradle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
            menu.add(newtonsCradle);
            newtonsCradle.addActionListener(new MenuInput(gameScreen));

            JMenuItem particleExplosion = new JMenuItem("Particle explosion");
            particleExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.ALT_DOWN_MASK));
            menu.add(particleExplosion);
            particleExplosion.addActionListener(new MenuInput(gameScreen));

            JMenuItem proximityExplosion = new JMenuItem("Proximity explosion");
            proximityExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
            menu.add(proximityExplosion);
            proximityExplosion.addActionListener(new MenuInput(gameScreen));

            JMenuItem raycastExplosion = new JMenuItem("Raycast explosion");
            raycastExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
            menu.add(raycastExplosion);
            raycastExplosion.addActionListener(new MenuInput(gameScreen));

            JMenuItem raycast = new JMenuItem("Raycast");
            raycast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
            menu.add(raycast);
            raycast.addActionListener(new MenuInput(gameScreen));

            JMenuItem restitution = new JMenuItem("Restitution");
            restitution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));
            menu.add(restitution);
            restitution.addActionListener(new MenuInput(gameScreen));

            JMenuItem stackedObjects = new JMenuItem("Stacked objects");
            stackedObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK));
            menu.add(stackedObjects);
            stackedObjects.addActionListener(new MenuInput(gameScreen));

            JMenuItem trebuchet = new JMenuItem("Trebuchet");
            trebuchet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.ALT_DOWN_MASK));
            menu.add(trebuchet);
            trebuchet.addActionListener(new MenuInput(gameScreen));

            JMenuItem wreckingBall = new JMenuItem("Wrecking ball");
            wreckingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
            menu.add(wreckingBall);
            wreckingBall.addActionListener(new MenuInput(gameScreen));

            window.setVisible(true);
        }
    }
}