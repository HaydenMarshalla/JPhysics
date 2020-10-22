package testbed.demo;

public class Main {
    public static void main(String[] args) {
        TestBedWindow demoWindow = new TestBedWindow(true);
        TestBedWindow.showWindow(demoWindow, "2D Physics Engine Demo", 1280,720);
        demoWindow.startThread();
    }
}