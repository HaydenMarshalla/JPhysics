package testbed.demo;

public class Main {
    public static void main(String[] args) {
        demoWindow demoWindow = new demoWindow(1280, 720, true);
        testbed.demo.demoWindow.showWindow(demoWindow, "2D Physics Engine Demo");
    }
}