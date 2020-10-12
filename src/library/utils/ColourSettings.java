package library.utils;

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
        drawJoints = new Color(255, 255, 255, 255);
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
    }

    //All objects
    public Color aabb;
    public Color centreOfMass;
    public Color contactNormals;
    public Color contactImpulse;
    public Color frictionImpulse;
    public Color trail;

    //Static objects
    public Color staticFill;
    public Color staticOutLine;

    //Non static objects
    public Color shapeFill;
    public Color shapeOutLine;
    public Color drawJoints;

    //Proximity explosion
    public Color proximity;
    public Color linesToObjects;
    public final int CIRCLE_RADIUS = 3;

    //Rays
    public Color rayToBody;
    public Color projectedRay;
    public final double rayEndPointCircleSize = 2;

    //Testbed related drawing
    public Color gridLines;
    public Color gridAxis;
    public Color background;
    public Stroke axisStrokeWidth = new BasicStroke(2);
    public Stroke defaultStrokeWidth = new BasicStroke(1);
}