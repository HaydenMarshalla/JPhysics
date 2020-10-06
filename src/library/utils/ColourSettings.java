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
        staticFill = new Color(120, 233, 233, 255);
        staticOutLine = new Color(240, 73, 70, 255);
        proximity = new Color(255, 255, 0, 200);
        linesToObjects = new Color(255, 255, 0, 100);
        trail = new Color(255, 255, 0, 255);
        rayToBody = new Color(255, 255, 255, 255);
        projectedRay = new Color(127, 127, 127, 150);
    }

    public Color background;
    public Color shapeOutLine;
    public Color aabb;
    public Color shapeFill;
    public Color drawJoints;
    public Color contactNormals;
    public Color contactImpulse;
    public Color frictionImpulse;
    public Color centreOfMass;
    public Color proximity;
    public Color linesToObjects;
    public Color rayToBody;
    public Color projectedRay;

    public Color trail;

    public Color staticFill;
    public Color staticOutLine;

    //The circle radius drawn at the end of a line to an object.
    public final int CIRCLE_RADIUS = 5;
    public final double rayEndPointCircleSize = 2;
}