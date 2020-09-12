package library;

import library.math.Vectors2D;
import testbed.demo.TestBedWindow;

public class Camera {
    public double zoom;
    public int width;
    public int height;
    public Vectors2D centre;
    private TestBedWindow panel;

    public Camera(int windowWidth, int windowHeight, TestBedWindow testWindow) {
        centre = new Vectors2D(0, 0);
        zoom = 1.0;
        this.width = windowWidth;
        this.height = windowHeight;
        panel = testWindow;
    }

    public Vectors2D scaleToScreen(Vectors2D v) {
        double aspectRatio = width * 1.0 / height;
        Vectors2D extents = new Vectors2D(aspectRatio * 200, 200);
        extents = extents.scalar(zoom);
        Vectors2D upperBound = centre.addi(extents);
        Vectors2D lowerBound = centre.subtract(extents);
        double w = (v.x - lowerBound.x) / (upperBound.x - lowerBound.x);
        double h = (v.y - lowerBound.y) / (upperBound.y - lowerBound.y);

        Vectors2D output = new Vectors2D();
        output.x = w * panel.getWidth();
        output.y = (1.0 - h) * (panel.getWidth()/aspectRatio);
        return output;
    }

    public double scaleToScreenXValue(double radius) {
        double aspectRatio = width * 1.0 / height;
        Vectors2D extents = new Vectors2D(aspectRatio *  200, 200);
        extents = extents.scalar(zoom);
        Vectors2D upperBound = centre.addi(extents);
        Vectors2D lowerBound = centre.subtract(extents);
        double w = radius / (upperBound.x - lowerBound.x);
        return w * panel.getWidth();
    }

    public void transformCentre(Vectors2D v) {
        centre.add(v);
    }

    public void setCentre(Vectors2D centre){
        this.centre = centre;
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }
}