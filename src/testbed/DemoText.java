package testbed;

import testbed.demo.tests.*;

import java.awt.*;

public class DemoText {
    public static void draw(Graphics2D g, ColourSettings paintSettings, int demo) {
        if (!paintSettings.getDrawText()) {
            return;
        }
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.PLAIN, 20));
        switch (demo) {
            case 0 -> drawArray(Chains.text, g);
            case 1 -> {
                drawArray(LineOfSight.text, g);
                LineOfSight.setRayCount();
            }
            case 2 -> drawArray(ParticleExplosionTest.text, g);
            case 3 -> drawArray(ProximityExplosionTest.text, g);
            case 4 -> drawArray(RaycastExplosionTest.text, g);
            case 5 -> drawArray(Raycast.text, g);
            case 6 -> drawArray(Trebuchet.text, g);
            case 7 -> drawArray(SliceObjects.text, g);
            case 8 -> drawArray(BouncingBall.text, g);
            case 9 -> drawArray(MixedShapes.text, g);
            case 10 -> drawArray(NewtonsCradle.text, g);
            case 11 -> drawArray(WreckingBall.text, g);
            case 12 -> drawArray(Friction.text, g);
            case 13 -> drawArray(Drag.text, g);
            case 14 -> drawArray(Restitution.text, g);
            case 15 -> drawArray(StackedObjects.text, g);
        }
    }

    public static void drawArray(String[] lines, Graphics2D g) {
        int y = 20;
        for (String line : lines) {
            g.drawString(line, 5, y);
            y += 20;
        }
        g.drawString("Right click: moves the camera position", 5, y += 0);
        g.drawString("Space: pauses demo", 5, y += 20);
    }
}
