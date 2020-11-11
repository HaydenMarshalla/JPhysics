package testbed;

import java.awt.*;

public class ColourSettings {
    public ColourSettings() {
        matterjsColourScheme();
    }

    public void defaultColourScheme() {
        aabb = new Color(255, 255, 255, 255);
        shapeFill = new Color(133, 49, 64, 255);
        shapeOutLine = new Color(201, 151, 157, 255);
        background = new Color(48, 31, 46, 255);
        joints = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        contactImpulse = new Color(255, 255, 255, 255);
        frictionImpulse = new Color(255, 255, 255, 255);
        centreOfMass = new Color(201, 151, 157, 255);
        staticFill = new Color(37, 66, 31, 255);
        staticOutLine = new Color(98, 142, 83, 255);
        proximity = new Color(255, 255, 0, 200);
        linesToObjects = new Color(255, 255, 0, 100);
        trail = new Color(255, 255, 0, 200);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        contactPoint = new Color(255, 255, 255, 255);
        scatterRays = new Color(255, 255, 0, 255);
    }

    public void box2dColourScheme() {
        aabb = new Color(255, 255, 255, 255);
        shapeFill = new Color(57,44,44, 255);
        shapeOutLine = new Color(229,178,178, 255);
        background = new Color(0,0,0, 255);
        joints = new Color(127,204,204, 255);
        contactNormals = new Color(255, 255, 255, 255);
        contactImpulse = new Color(255, 255, 255, 255);
        frictionImpulse = new Color(255, 255, 255, 255);
        centreOfMass = new Color(231,178,177, 255);
        staticFill = new Color(33,57,29, 255);
        staticOutLine = new Color(124,230,129, 255);
        proximity = new Color(255, 255, 0, 200);
        linesToObjects = new Color(255, 255, 0, 100);
        trail = new Color(255, 255, 0, 200);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        contactPoint = new Color(255, 255, 255, 255);
        scatterRays = new Color(255, 255, 0, 255);
    }

    public void matterjsColourScheme() {
        aabb = new Color(39,31,17, 255);
        shapeFill = new Color(15,15,19, 255);
        shapeOutLine = new Color(101,101,103, 255);
        background = new Color(15,15,19, 255);
        joints = new Color(255,255,255, 255);

        contactPoint = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        contactImpulse = new Color(255, 255, 255, 255);
        frictionImpulse = new Color(255, 255, 255, 255);

        centreOfMass = new Color(206,92,92, 255);
        staticFill = new Color(15,15,19, 255);
        staticOutLine = new Color(101,101,103, 255);
        proximity = new Color(255, 255, 255, 255);
        linesToObjects = new Color(255, 255, 255, 255);
        trail = new Color(255, 255, 255, 200);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        scatterRays = new Color(255, 255, 0, 255);
    }

    //All objects
    public Color aabb;
    public Color centreOfMass;
    public Color contactPoint;
    public Color contactNormals;
    public final double NORMAL_LINE_SCALAR = 2.0;
    public Color contactImpulse;
    public Color frictionImpulse;
    public Color trail;
    public final int COM_RADIUS = 5;

    //Static objects
    public Color staticFill;
    public Color staticOutLine;

    //Non static objects
    public Color shapeFill;
    public Color shapeOutLine;
    public Color joints;

    //Proximity explosion
    public Color proximity;
    public Color linesToObjects;
    public final int CIRCLE_RADIUS = 3;

    //Rays
    public Color rayToBody;
    public Color projectedRay;
    public final double RAY_DOT = 2;
    public Color scatterRays;

    //Testbed related drawing
    public final Color gridLines = new Color(255, 255, 255, 20);
    public final Color gridAxis = new Color(255, 255, 255, 130);
    public Color background;
    public Stroke axisStrokeWidth = new BasicStroke(2);
    public Stroke defaultStrokeWidth = new BasicStroke(1);

    //Flags for testbed
    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = true;
    private boolean drawContactPoints = true;
    private boolean drawContactNormals = true;
    private boolean drawCOMs = true;
    private boolean drawGrid = false;

    public final double CONTACT_LINE_SCALAR = 2.0;

    public boolean getDrawShapes() {
        return drawShapes;
    }

    public void setDrawShapes(boolean drawShapes) {
        this.drawShapes = drawShapes;
    }

    public boolean getDrawJoints() {
        return drawJoints;
    }

    public void setDrawJoints(boolean drawJoints) {
        this.drawJoints = drawJoints;
    }

    public boolean getDrawAABBs() {
        return drawAABBs;
    }

    public void setDrawAABBs(boolean drawAABBs) {
        this.drawAABBs = drawAABBs;
    }

    public boolean getDrawContactPoints() {
        return drawContactPoints;
    }

    public void setDrawContactPoints(boolean drawContactPoints) {
        this.drawContactPoints = drawContactPoints;
    }

    public boolean getDrawContactNormals() {
        return drawContactNormals;
    }

    public void setDrawContactNormals(boolean drawContactNormals) {
        this.drawContactNormals = drawContactNormals;
    }

    public boolean getDrawCOMs() {
        return drawCOMs;
    }

    public void setDrawCOMs(boolean drawCOMs) {
        this.drawCOMs = drawCOMs;
    }

    public boolean getDrawGrid() {
        return drawGrid;
    }

    public void setDrawGrid(boolean drawGrid) {
        this.drawGrid = drawGrid;
    }
}