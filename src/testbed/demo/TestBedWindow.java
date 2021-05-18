package testbed.demo;

import library.collision.AABB;
import library.dynamics.*;
import library.explosions.Explosion;
import library.explosions.ParticleExplosion;
import library.geometry.Circle;
import library.geometry.Polygon;
import library.joints.Joint;
import library.rays.Ray;
import library.rays.ShadowCasting;
import library.rays.Slice;
import library.dynamics.Settings;
import testbed.ColourSettings;
import library.math.Vectors2D;
import testbed.Camera;
import testbed.DemoText;
import testbed.Trail;
import testbed.demo.input.*;
import testbed.demo.tests.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Iterator;

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

        Chains.load(this);
    }

    public void startThread() {
        PHYSICS_THREAD.start();
    }

    private final ArrayList<Ray> rays = new ArrayList<>();

    public void add(Ray ray) {
        rays.add(ray);
    }

    private final ArrayList<Slice> slices = new ArrayList<>();

    public void add(Slice s) {
        slices.add(s);
    }

    public int getSlicesSize() {
        return slices.size();
    }

    public ArrayList<Slice> getSlices() {
        return slices;
    }

    private final ArrayList<Explosion> explosionObj = new ArrayList<>();

    public ArrayList<Explosion> getRayExplosions() {
        return explosionObj;
    }

    public void add(Explosion ex) {
        explosionObj.add(ex);
    }

    private final ArrayList<ParticleExplosion> particles = new ArrayList<>();

    public void add(ParticleExplosion p, double lifespan) {
        particles.add(p);
        for (Body b : p.getParticles()) {
            trailsToBodies.add(new Trail(1000, 1, b, lifespan));
        }
    }

    private final ArrayList<ShadowCasting> shadowCastings = new ArrayList<>();

    public void add(ShadowCasting shadowCasting) {
        shadowCastings.add(shadowCasting);
    }

    private World world = new World();

    public void setWorld(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    private final ArrayList<Trail> trailsToBodies = new ArrayList<>();

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

    private void updateRays() {
        for (Ray r : rays) {
            if (Raycast.active) {
                Raycast.action(r);
            }
            r.updateProjection(world.bodies);
        }
        for (Explosion p : explosionObj) {
            p.update(world.bodies);
        }
        for (ShadowCasting s : shadowCastings) {
            s.updateProjections(world.bodies);
        }
        for (Slice s : slices) {
            s.updateProjection(world.bodies);
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
            repaint();
        }
    }

    private void update() {
        double dt = Settings.HERTZ > 0.0 ? 1.0 / Settings.HERTZ : 0.0;
        world.step(dt);
        updateTrails();
        updateRays();
        checkParticleLifetime(dt);
    }

    private void checkParticleLifetime(double timePassed) {
        ArrayList<Body> bodiesToRemove = new ArrayList<>();
        Iterator<Trail> i = trailsToBodies.iterator();
        while (i.hasNext()) {
            Trail s = i.next();
            if (s.checkLifespan(timePassed)) {
                bodiesToRemove.add(s.getBody());
                i.remove();
            }
        }
        Iterator<ParticleExplosion> p = particles.iterator();
        while (p.hasNext()) {
            Body[] s = p.next().getParticles();
            if (containsBody(s, bodiesToRemove)) {
                removeParticlesFromWorld(s);
                p.remove();
            }
        }
    }

    private void removeParticlesFromWorld(Body[] s) {
        for (Body b : s) {
            world.removeBody(b);
        }
    }

    private boolean containsBody(Body[] s, ArrayList<Body> bodiesToRemove) {
        for (Body a : s) {
            if (bodiesToRemove.contains(a)) {
                return true;
            }
        }
        return false;
    }

    public void clearTestbedObjects() {
        CAMERA.reset();
        world.clearWorld();
        trailsToBodies.clear();
        rays.clear();
        explosionObj.clear();
        shadowCastings.clear();
        slices.clear();
        repaint();
    }


    public final ColourSettings PAINT_SETTINGS = new ColourSettings();

    public ColourSettings getPAINT_SETTINGS() {
        return PAINT_SETTINGS;
    }

    private int currentDemo = 0;

    public void setCurrentDemo(int i) {
        currentDemo = i;
    }
    public boolean followPayload = false;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (ANTIALIASING) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        setBackground(PAINT_SETTINGS.background);
        update();
        if (followPayload){
            setCamera(new Vectors2D(world.bodies.get(3).position.x,getCamera().centre.y) , 2.0);
        }
        if (PAINT_SETTINGS.getDrawGrid()) {
            drawGridMethod(g2d);
        }
        for (ShadowCasting s : shadowCastings) {
            s.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        drawTrails(g2d);
        for (Body b : world.bodies) {
            if (PAINT_SETTINGS.getDrawShapes()) {
                b.shape.draw(g2d, PAINT_SETTINGS, CAMERA);
            }
            if (PAINT_SETTINGS.getDrawAABBs()) {
                b.shape.drawAABB(g2d, PAINT_SETTINGS, CAMERA);
            }
            if (PAINT_SETTINGS.getDrawCOMs()) {
                b.shape.drawCOMS(g2d, PAINT_SETTINGS, CAMERA);
            }
        }
        if (PAINT_SETTINGS.getDrawContacts()) {
            world.drawContact(g2d, PAINT_SETTINGS, CAMERA);
        }
        if (PAINT_SETTINGS.getDrawJoints()) {
            for (Joint j : world.joints) {
                j.draw(g2d, PAINT_SETTINGS, CAMERA);
            }
        }
        for (Explosion p : explosionObj) {
            p.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        for (Ray r : rays) {
            r.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        for (Slice s : slices) {
            s.draw(g2d, PAINT_SETTINGS, CAMERA);
        }
        DemoText.draw(g2d, PAINT_SETTINGS, currentDemo);
    }

    private void drawGridMethod(Graphics2D g2d) {
        int projection = 20000;
        int spacing = 10;
        int minXY = -projection;
        int maxXY = projection;
        int totalProjectionDistance = projection + projection;
        g2d.setColor(PAINT_SETTINGS.gridLines);
        for (int i = 0; i <= totalProjectionDistance; i += spacing) {
            if (i == projection) {
                g2d.setStroke(PAINT_SETTINGS.axisStrokeWidth);
                g2d.setColor(PAINT_SETTINGS.gridAxis);
            }

            Vectors2D currentMinY = CAMERA.convertToScreen(new Vectors2D(minXY + i, minXY));
            Vectors2D currentMaxY = CAMERA.convertToScreen(new Vectors2D(minXY + i, maxXY));
            g2d.draw(new Line2D.Double(currentMinY.x, currentMinY.y, currentMaxY.x, currentMaxY.y));

            Vectors2D currentMinX = CAMERA.convertToScreen(new Vectors2D(minXY, minXY + i));
            Vectors2D currentMaxX = CAMERA.convertToScreen(new Vectors2D(maxXY, minXY + i));
            g2d.draw(new Line2D.Double(currentMinX.x, currentMinX.y, currentMaxX.x, currentMaxX.y));

            if (i == projection) {
                g2d.setStroke(PAINT_SETTINGS.defaultStrokeWidth);
                g2d.setColor(PAINT_SETTINGS.gridLines);
            }
        }
    }

    private void drawTrails(Graphics2D g) {
        g.setColor(PAINT_SETTINGS.trail);
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
            gameScreen.setBackground(gameScreen.PAINT_SETTINGS.background);

            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createTestMenu(gameScreen));
            menuBar.add(createColourSchemeMenu(gameScreen));
            menuBar.add(createFrequencyMenu(gameScreen));
            menuBar.add(createDisplayMenu(gameScreen));
            window.setJMenuBar(menuBar);

            window.setVisible(true);
        }
    }

    private static Component createDisplayMenu(TestBedWindow gameScreen) {
        JMenu drawOptions = new JMenu("Graphics Options");

        JMenuItem showGrid = new JMenuItem("Display Grid");
        drawOptions.add(showGrid);
        showGrid.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem displayShapes = new JMenuItem("Display Shapes");
        drawOptions.add(displayShapes);
        displayShapes.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem displayJoints = new JMenuItem("Display Joints");
        drawOptions.add(displayJoints);
        displayJoints.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem displayAABBs = new JMenuItem("Display AABBs");
        drawOptions.add(displayAABBs);
        displayAABBs.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem displayContactPoints = new JMenuItem("Display Contacts");
        drawOptions.add(displayContactPoints);
        displayContactPoints.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem displayCOMs = new JMenuItem("Display COMs");
        drawOptions.add(displayCOMs);
        displayCOMs.addActionListener(new ColourMenuInput(gameScreen));

        return drawOptions;
    }

    private static Component createFrequencyMenu(TestBedWindow gameScreen) {
        JMenu hertzMenu = new JMenu("Hertz");
        int number = 30;
        for (int i = 1; i < 5; i++) {
            JMenuItem hertzMenuItem = new JMenuItem("" + number * i);
            hertzMenu.add(hertzMenuItem);
            hertzMenuItem.addActionListener(e -> {
                switch (e.getActionCommand()) {
                    case "30" -> Settings.HERTZ = 30;
                    case "60" -> Settings.HERTZ = 60;
                    case "90" -> Settings.HERTZ = 90;
                    case "120" -> Settings.HERTZ = 120;
                }
            });
        }
        return hertzMenu;
    }

    private static JMenu createColourSchemeMenu(TestBedWindow gameScreen) {
        JMenu colourScheme = new JMenu("Colour schemes");

        JMenuItem defaultScheme = new JMenuItem("Default");
        colourScheme.add(defaultScheme);
        defaultScheme.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem box2dScheme = new JMenuItem("Box2d");
        colourScheme.add(box2dScheme);
        box2dScheme.addActionListener(new ColourMenuInput(gameScreen));

        JMenuItem monochromaticScheme = new JMenuItem("Monochromatic");
        colourScheme.add(monochromaticScheme);
        monochromaticScheme.addActionListener(new ColourMenuInput(gameScreen));

        return colourScheme;
    }

    private static JMenu createTestMenu(TestBedWindow gameScreen) {
        JMenu testMenu = new JMenu("Demos");
        testMenu.setMnemonic(KeyEvent.VK_M);

        JMenuItem chains = new JMenuItem("Chains");
        chains.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
        testMenu.add(chains);
        chains.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem lineOfSight = new JMenuItem("Line of sight");
        lineOfSight.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.ALT_DOWN_MASK));
        testMenu.add(lineOfSight);
        lineOfSight.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem particleExplosion = new JMenuItem("Particle explosion");
        particleExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.ALT_DOWN_MASK));
        testMenu.add(particleExplosion);
        particleExplosion.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem proximityExplosion = new JMenuItem("Proximity explosion");
        proximityExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.ALT_DOWN_MASK));
        testMenu.add(proximityExplosion);
        proximityExplosion.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem raycastExplosion = new JMenuItem("Raycast explosion");
        raycastExplosion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.ALT_DOWN_MASK));
        testMenu.add(raycastExplosion);
        raycastExplosion.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem raycast = new JMenuItem("Raycast");
        raycast.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, InputEvent.ALT_DOWN_MASK));
        testMenu.add(raycast);
        raycast.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem trebuchet = new JMenuItem("Trebuchet");
        trebuchet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, InputEvent.ALT_DOWN_MASK));
        testMenu.add(trebuchet);
        trebuchet.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem sliceObjects = new JMenuItem("Slice objects");
        sliceObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, InputEvent.ALT_DOWN_MASK));
        testMenu.add(sliceObjects);
        sliceObjects.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem bouncingBall = new JMenuItem("Bouncing ball");
        bouncingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, InputEvent.ALT_DOWN_MASK));
        testMenu.add(bouncingBall);
        bouncingBall.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem mixedShapes = new JMenuItem("Mixed shapes");
        mixedShapes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
        testMenu.add(mixedShapes);
        mixedShapes.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem newtonsCradle = new JMenuItem("Newtons cradle");
        newtonsCradle.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.ALT_DOWN_MASK));
        testMenu.add(newtonsCradle);
        newtonsCradle.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem wreckingBall = new JMenuItem("Wrecking ball");
        wreckingBall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_DOWN_MASK));
        testMenu.add(wreckingBall);
        wreckingBall.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem friction = new JMenuItem("Friction");
        friction.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
        testMenu.add(friction);
        friction.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem drag = new JMenuItem("Drag");
        drag.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
        testMenu.add(drag);
        drag.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem restitution = new JMenuItem("Restitution");
        restitution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));
        testMenu.add(restitution);
        restitution.addActionListener(new KeyBoardInput(gameScreen));

        JMenuItem stackedObjects = new JMenuItem("Stacked objects");
        stackedObjects.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK));
        testMenu.add(stackedObjects);
        stackedObjects.addActionListener(new KeyBoardInput(gameScreen));

        return testMenu;
    }

    public void generateRandomObjects(Vectors2D lowerBound, Vectors2D upperBound, int totalObjects, int maxRadius) {
        while (totalObjects > 0) {
            Body b = createRandomObject(lowerBound, upperBound, maxRadius);
            if (overlap(b)) {
                world.addBody(b);
                totalObjects--;
            }
        }
    }

    public void generateBoxOfObjects() {
        {
            Body top = new Body(new Polygon(900.0, 20.0), -20, 500);
            top.setDensity(0);
            world.addBody(top);

            Body right = new Body(new Polygon(500.0, 20.0), 900, 20);
            right.setOrientation(1.5708);
            right.setDensity(0);
            world.addBody(right);

            Body bottom = new Body(new Polygon(900.0, 20.0), 20, -500);
            bottom.setDensity(0);
            world.addBody(bottom);

            Body left = new Body(new Polygon(500.0, 20.0), -900, -20);
            left.setOrientation(1.5708);
            left.setDensity(0);
            world.addBody(left);
        }

        {
            generateRandomObjects(new Vectors2D(-880, -480), new Vectors2D(880, 480), 30, 100);
            setStaticWorldBodies();
        }
    }

    private boolean overlap(Body b) {
        for (Body a : world.bodies) {
            if (AABB.AABBOverLap(a, b)) {
                return false;
            }
        }
        return true;
    }

    private Body createRandomObject(Vectors2D lowerBound, Vectors2D upperBound, int maxRadius) {
        int objectType = Settings.generateRandomNoInRange(1, 2);
        Body b = null;
        int radius = Settings.generateRandomNoInRange(5, maxRadius);
        double x = Settings.generateRandomNoInRange(lowerBound.x + radius, upperBound.x - radius);
        double y = Settings.generateRandomNoInRange(lowerBound.y + radius, upperBound.y - radius);
        double rotation = Settings.generateRandomNoInRange(0.0, 7.0);
        switch (objectType) {
            case 1:
                b = new Body(new Circle(radius), x, y);
                b.setOrientation(rotation);
                break;
            case 2:
                int sides = Settings.generateRandomNoInRange(3, 10);
                b = new Body(new Polygon(radius, sides), x, y);
                b.setOrientation(rotation);
                break;
        }
        return b;
    }

    public void setStaticWorldBodies() {
        for (Body b : world.bodies) {
            b.setDensity(0);
        }
    }

    public void buildExplosionDemo() {
        {
            buildShelf(50.0, 300.0);
            buildShelf(450.0, 400.0);
        }

        Body floor = new Body(new Polygon(20000.0, 2000.0), 0, -2000);
        floor.setDensity(0);
        world.addBody(floor);

        Body reflect = new Body(new Polygon(40.0, 5.0), -100, 330);
        reflect.setOrientation(0.785398);
        reflect.setDensity(0);
        world.addBody(reflect);

        {
            Body top = new Body(new Polygon(120.0, 10.0), 450, 210);
            top.setDensity(0);
            world.addBody(top);

            Body side1 = new Body(new Polygon(100.0, 10.0), 340, 100);
            side1.setOrientation(1.5708);
            side1.setDensity(0);
            world.addBody(side1);

            Body side2 = new Body(new Polygon(100.0, 10.0), 560, 100);
            side2.setOrientation(1.5708);
            side2.setDensity(0);
            world.addBody(side2);

            for (int i = 0; i < 4; i++) {
                Body box = new Body(new Polygon(20.0, 20.0), 450, 20 + (i * 40));
                world.addBody(box);
            }
        }

        for (int k = 0; k < 2; k++) {
            for (int i = 0; i < 5; i++) {
                Body box = new Body(new Polygon(20.0, 20.0), -600 + (k * 200), 20 + (i * 40));
                world.addBody(box);
            }
        }
    }

    public void buildShelf(double x, double y) {
        Body shelf = new Body(new Polygon(100.0, 10.0), x, y);
        shelf.setDensity(0);
        world.addBody(shelf);

        int boxes = 4;
        for (int i = 0; i < boxes; i++) {
            Body box = new Body(new Polygon(10.0, 20.0), x, y + 30 + (i * 40));
            world.addBody(box);
        }
    }

    public void createPyramid(int noOfPillars, int x, int y) {
        double height = 30.0;
        double width = 5.0;
        x += width;

        double widthOfTopPillar = height + height;
        for (int k = 0; k < noOfPillars; k++) {
            x += height;

            Body initialPillar = new Body(new Polygon(width + 2, height), x, y + height);
            addPillar(initialPillar);

            for (int i = 0; i < noOfPillars - k; i++) {
                Body rightPillar = new Body(new Polygon(width + 2, height), x + widthOfTopPillar + (widthOfTopPillar * i), y + height);
                addPillar(rightPillar);

                Body topPillar = new Body(new Polygon(height, width), x + height + (i * widthOfTopPillar), y + widthOfTopPillar + width);
                addPillar(topPillar);
            }
            y += widthOfTopPillar + width + width;
        }
    }

    public void createTower(int floors, int x, int y) {
        double height = 30.0;
        double width = 5.0;
        x += width;

        double heightOfPillar = height + height;
        double widthOfPillar = width + width;
        for (int k = 0; k < floors; k++) {
            Body leftPillar = new Body(new Polygon(width, height), x, y + height);
            addPillar(leftPillar);

            Body rightPillar = new Body(new Polygon(width, height), x + heightOfPillar - widthOfPillar, y + height);
            addPillar(rightPillar);

            Body topPillar = new Body(new Polygon(height, width), x + height - width, y + heightOfPillar + width);
            addPillar(topPillar);
            y += heightOfPillar + width + width;
        }
    }

    //Removing some boiler plate for create tower and Pyramid
    private void addPillar(Body b) {
        b.restitution = 0.2;
        b.setDensity(0.2);
        world.addBody(b);
    }

    //Removes friction from the world
    public void setWorldIce() {
        for (Body b : world.bodies) {
            b.staticFriction = 0.0;
            b.dynamicFriction = 0.0;
        }
    }

    // Scaled friction by a passed ratio
    public void scaleWorldFriction(double ratio) {
        for (Body b : world.bodies) {
            b.staticFriction *= ratio;
            b.dynamicFriction *= ratio;
        }
    }
}