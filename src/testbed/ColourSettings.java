package testbed;

import java.awt.*;

public class ColourSettings {
    public ColourSettings() {
        defaultColourScheme();
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
        trail = new Color(255, 255, 0, 255);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        gridLines = new Color(255, 255, 255, 70);
        gridAxis = new Color(255, 255, 255, 150);
        contactPoint = new Color(255, 255, 255, 255);
    }

    //All objects
    public Color aabb;
    public Color centreOfMass;
    public Color contactPoint;
    public Color contactNormals;
    public final double NORMAL_SCALAR = 50.0;
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

    //Testbed related drawing
    public Color gridLines;
    public Color gridAxis;
    public Color background;
    public Stroke axisStrokeWidth = new BasicStroke(2);
    public Stroke defaultStrokeWidth = new BasicStroke(1);

    //Flags for testbed
    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = false;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
    private boolean drawContactImpulse = false;
    private boolean drawFrictionImpulse = false;
    private boolean drawCOMs = false;
    private boolean drawGrid = false;

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

    public boolean getDrawContactImpulse() {
        return drawContactImpulse;
    }

    public void setDrawContactImpulse(boolean drawContactImpulse) {
        this.drawContactImpulse = drawContactImpulse;
    }

    public boolean getDrawFrictionImpulse() {
        return drawFrictionImpulse;
    }

    public void setDrawFrictionImpulse(boolean drawFrictionImpulse) {
        this.drawFrictionImpulse = drawFrictionImpulse;
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