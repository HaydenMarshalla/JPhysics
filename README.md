# JPhysics
JPhysics is a 2D physics engine with no third-party-library dependencies.

The engine is written in Java and has been created with the intention of being used in games.

## Tech demos

The testbed includes numerous tech demos to show what the engine is capable of. Some examples are given below:
![a relative link](Images/Chains%20demo.png?raw=true)

## Feature List
### Physics
- Rigid body dynamics
- Primitive joint constraints
- Momentum
- Friction
- Restitution
- Collision response (Sequential Impulses Solver)
- Stable object stacking
- Orbits
- Explosions

### Collision
- AABB queries (Broadphase)
- One-shot contact manifolds
- Discrete collision detection
- Convex polygon and circle collisions
- Ray casting
- Position resolution handling

### Explosion types
- Proximity
- Ray casting
- Particle

### Testbed
- Java swing for demo graphics
- Junit4 for junit tests

### Future features to implement
- Object slicing
- Multi body shapes
    - collisions optimizations for said shapes
- Demos illustrating limitations within the engine
- Dynamic tree broadphase
- Continuous collision detection
- Island solution and sleep management
- Extra types of joints (eg Revolute, pulley)
- Fluid and soft body simulation

## Controls for testbed demos

When a tech demo is active, certain buttons can be pressed to interact with the world as follows;

Trebuchet: Press b to release projectile

Explosion demos: Click to cast an explosion at the location clicked

The camera can be repositioned by holding down right mouse button and dragging across the screen.

All demos can be paused by pressing the space bar.

## Getting Started
Follow these simple steps!

### Prerequisites
- An appropriate IDE for example Intellij (with java 1.8+ JDK installed)
- Junit4 library (for junit tests)

#### Main files
All you need to do is clone the repository and place the JPhysics/src files in the source directory of your chosen IDE.

#### Adding JUnit library

For JUnit 4, you will need to add junit4 jar file to class path. This can be done as follows in intellij:

Go to file -> project structure (Ctrl-Alt-Shift-S)

Click the plus button at the top and select "From Maven as shown below"

![a relative link](Images/Maven.png?raw=true)

In the search box, type in "junit:junit:4.12" and press ok. This is the JUnit dependency I use and have tested on.

After this, Keep hitting OK until you're back to the original project code.

### Testing
Now you can go to "src/testbed/junittests/" and run the tests!

## Authors
Hayden Marshall

## License
The repository falls under the [MIT license](https://en.wikipedia.org/wiki/MIT_License). See LICENSE.txt for more information.

## Credits
Dirk Gregorius and Erin Catto's gdc talks and documentation have been of great help with the theoretical approach of creating a physics engine.
