# Ray Tracing Engine

## Overview
This project is a ray tracing engine developed in Java 17 as part of a Practical course for the Intro to Software Engineering. The main objective is to explore advanced software engineering concepts while building a functional ray tracing engine.

## Features
- Multi-threading: The engine uses multi-threading to optimize performance in the final stage.
- Ray Tracing: Simulates the behavior of light to generate realistic reflections, shadows, and other optical effects.
- Customizable Scenes: Users can define various geometries and light sources in a scene.

## Project Structure
The project is organized into several packages:
- geometries: Contains classes that define the various geometric shapes in the scene (e.g., spheres, planes).
- renderer: Handles the core logic of rendering the scene and tracing rays.
- scene: Defines the components of a scene, such as the camera, objects, and light sources.

## Design Patterns
The project implements several key design patterns to ensure flexibility and maintainability:
- Builder Pattern: Used to construct complex objects like scenes and geometries.
- Composite Pattern: Allows for grouping of objects (e.g., shapes) so that they can be treated as a single entity.
- Wrapper Pattern: Enhances or modifies the behavior of objects without altering their structure.

## Installation
To run this project, you'll need:
- Java 17 or higher installed on your system.

### Running the project:
Simply compile and run the project using the following command:
javac -cp . your_main_class.java
java your_main_class

## Learning Outcomes
Through this project, we explored several advanced software engineering concepts:
- Test-Driven Development (TDD): Focused on building robust and reliable code using test-first approaches.
- Software Development: Gained experience in high-level software design, architecture, and implementation.
  
## Future Improvements
Potential future enhancements include:
- Adding support for more complex lighting models.
- Implementing further performance optimizations like GPU-based rendering.