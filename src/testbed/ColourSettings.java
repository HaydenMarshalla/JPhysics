package testbed;

import java.awt.*;

public class ColourSettings {
    public ColourSettings() {
        defaultColourScheme();
    }

    public void defaultColourScheme() {
        background = new Color(48, 31, 46, 255);
        shapeFill = new Color(133, 49, 64, 255);
        shapeOutLine = new Color(201, 151, 157, 255);
        staticFill = new Color(37, 66, 31, 255);
        staticOutLine = new Color(98, 142, 83, 255);

        aabb = new Color(255, 255, 255, 255);
        joints = new Color(255, 255, 255, 255);

        contactPoint = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        centreOfMass = new Color(201, 151, 157, 255);
        trail = new Color(255, 255, 0, 200);

        proximity = new Color(255, 255, 0, 200);
        linesToObjects = new Color(255, 255, 0, 100);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        scatterRays = new Color(255, 255, 0, 255);
    }

    public void box2dColourScheme() {
        background = new Color(0, 0, 0, 255);
        shapeFill = new Color(57, 44, 44, 255);
        shapeOutLine = new Color(229, 178, 178, 255);
        staticFill = new Color(33, 57, 29, 255);
        staticOutLine = new Color(124, 230, 129, 255);

        aabb = new Color(255, 255, 255, 255);
        joints = new Color(127, 204, 204, 255);

        contactPoint = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        centreOfMass = new Color(231, 178, 177, 255);
        trail = new Color(255, 255, 0, 200);

        proximity = new Color(255, 255, 0, 200);
        linesToObjects = new Color(255, 255, 0, 100);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        scatterRays = new Color(255, 255, 0, 255);
    }

    public void matterjsColourScheme() {
        background = new Color(15, 15, 19, 255);
        shapeFill = new Color(15, 15, 19, 255);
        shapeOutLine = new Color(101, 101, 103, 255);
        staticFill = new Color(15, 15, 19, 255);
        staticOutLine = new Color(101, 101, 103, 255);

        aabb = new Color(39, 31, 17, 255);
        joints = new Color(255, 255, 255, 255);

        contactPoint = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        centreOfMass = new Color(206, 92, 92, 255);
        trail = new Color(255, 255, 255, 200);

        proximity = new Color(255, 255, 255, 255);
        linesToObjects = new Color(255, 255, 255, 255);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
        scatterRays = new Color(255, 255, 0, 255);
    }

    public void monochromaticColourScheme() {
        background = new Color(0, 0, 0, 255);
        shapeFill = new Color(0, 0, 0, 255);
        shapeOutLine = new Color(255, 255, 255, 255);
        staticFill = shapeFill;
        staticOutLine = shapeOutLine;

        aabb = new Color(255, 255, 255, 255);
        joints = new Color(255, 255, 255, 255);

        contactPoint = new Color(255, 255, 255, 255);
        contactNormals = new Color(255, 255, 255, 255);
        centreOfMass = new Color(255, 255, 255, 255);
        trail = new Color(255, 255, 255, 255);

        proximity = new Color(255, 255, 255, 255);
        linesToObjects = new Color(255, 255, 255, 255);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(255, 255, 255, 255);
        scatterRays = new Color(255, 255, 255, 255);
    }

    //All objects
    public Color aabb;
    public Color centreOfMass;
    public Color contactPoint;
    public Color contactNormals;
    public Color trail;
    public final double NORMAL_LINE_SCALAR = 2.0;
    public final int COM_RADIUS = 5;
    public final double CONTACT_LINE_SCALAR = 2.0;

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
    public Color scatterRays;
    public final double RAY_DOT = 5;
    public final Color shadow = new Color(128,128,128, 126);


    //Testbed related drawing
    public final Color gridLines = new Color(255, 255, 255, 20);
    public final Color gridAxis = new Color(255, 255, 255, 130);
    public Color background;
    public Stroke axisStrokeWidth = new BasicStroke(2);
    public Stroke defaultStrokeWidth = new BasicStroke(1);

    //Flags for testbed
    private boolean drawShapes = true;
    private boolean drawJoints = true;
    private boolean drawAABBs = false;
    private boolean drawContactPoints = false;
    private boolean drawContactNormals = false;
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