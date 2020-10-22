# JPhysics
JPhysics is a 2D physics engine with no third-party-library dependencies.

The engine is written in Java and has been created with the intention of being used in games. 

## Tech demos

The testbed includes numerous tech demos to show what the engine is capable of. Some examples are given below:

## Feature List
### Physics
- Rigid body dynamics
- Primitive joint constraints
- Momentum
- Friction
- Restitution
- Collision response (Sequential Impulses Solver)
- Stable object stacking

### Collision
- AABB queries (Broadphase)
- One-shot contact manifolds
- Discrete collision detection
- Convex polygon and circle collisions

### Testbed
- Java swing for demo graphics
- Junit4 for junit tests

### To do
- Ray casting
- Object slicing
- Orbits
- Explosions
- Multi body shapes
    - collisions optimizations for said shapes
- Demos illustrating limitations within the engine
- Position resolution handling
- Dynamic tree broadphase
- Continuous collision detection
- Island solution and sleep management
- Extra types of joints (eg Revolute, pulley)
- Fluid and soft body simulation


## Getting Started
Follow these simple steps!

### Prerequisites
An appropriate IDE for example Intellij (with java 1.8+ JDK installed)
Junit4 library (for junit tests)

## Versioning Strategy
[Semantic versioning](https://semver.org/) has been used to tag the releases of the project.

## Authors
Hayden Marshall

## License
The repository falls under the [MIT license](https://en.wikipedia.org/wiki/MIT_License). See LICENSE.txt for more information.

## Credits
Dirk Gregorius and Erin Catto's gdc talks and documentation have been of great help with the theoretical approach of creating a physics engine.
