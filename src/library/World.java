package library;

import library.math.Vectors2D;

import java.util.ArrayList;

public class World {
    public Vectors2D gravity;
    public int iterations;

    public World(Vectors2D gravity, int iterations) {
        this.gravity = gravity;
        this.iterations = iterations;
    }

    public World(){
        gravity = new Vectors2D(0,0);
    }

    public ArrayList<Body> bodies = new ArrayList<>();
    public ArrayList<Arbiter> contacts = new ArrayList<>();

    public void step(double dt, int iterations) {
        contacts.clear();
    }

    public Body addBody(Body b) {
        bodies.add(b);
        return b;
    }

    public void removeBody() {
        
    }

    public void clearForces() {

    }
}