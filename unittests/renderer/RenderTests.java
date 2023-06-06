package renderer;

import static java.awt.Color.*;

import geometries.*;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.Random;

/** Test rendering a basic image
 * @author Dan */
public class RenderTests {

   /** Produce a scene with basic 3D model and render it into a png image with a
    * grid */
   @Test
   public void basicRenderTwoColorTest() {
      Scene scene = new Scene("Test scene")//
         .setAmbientLight(new AmbientLight(new Color(255, 191, 191), //
                                           new Double3(1, 1, 1))) //
         .setBackground(new Color(75, 127, 90));

      scene.geometries.add(new Sphere(50d, new Point(0, 0, -100)),
                           new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
                           // left
                           new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100),
                                        new Point(-100, -100, -100)), // down
                           // left
                           new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down
      // right
      Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
         .setVPDistance(100) //
         .setVPSize(500, 500) //
         .setImageWriter(new ImageWriter("base render test", 1000, 1000))
         .setRayTracer(new RayTracerBasic(scene));

      camera.renderImage();
      camera.printGrid(100, new Color(YELLOW));
      camera.writeToImage();
   }

   // For stage 6 - please disregard in stage 5
   /** Produce a scene with basic 3D model - including individual lights of the
    * bodies and render it into a png image with a grid */
   @Test
   public void basicRenderMultiColorTest() {
      Scene scene = new Scene("Test scene")//
         .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.2))); //

      scene.geometries.add( // center
                           new Sphere(50, new Point(0, 0, -100)),
                           // up left
                           new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                              .setEmission(new Color(GREEN)),
                           // down left
                           new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                              .setEmission(new Color(RED)),
                           // down right
                           new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                              .setEmission(new Color(BLUE)));

      Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
         .setVPDistance(100) //
         .setVPSize(500, 500) //
         .setImageWriter(new ImageWriter("color render test", 1000, 1000))
         .setRayTracer(new RayTracerBasic(scene));

      camera.renderImage();
      camera.printGrid(100, new Color(WHITE));
      camera.writeToImage();
   }


   private Scene scene1 = new Scene("Test scene");
   private Scene scene2 = new Scene("Test scene") //
           .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));
   private Camera camera1 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
           .setVPSize(150, 150) //
           .setVPDistance(1000);
   private Camera camera2 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
           .setVPSize(200, 200) //
           .setVPDistance(1000);
   private Material material = new Material().setKd(0.5).setKs(0.5).setShininess(300);
   private Point trPL = new Point(50, 30, -100); // Triangles test Position of Light
   private Point spPL = new Point(-50, -50, 25); // Sphere test Position of Light
   private Color trCL = new Color(800, 500, 250); // Triangles test Color of Light
   private Color spCL = new Color(800, 500, 0); // Sphere test Color of Light
   private Vector trDL = new Vector(-2, 2, -2); // Triangles test Direction of Light

   private Point[] square = { // The Triangles' vertices:
           new Point(-98, -110, -145), // the shared left-bottom
           new Point(97, 100, -145), // the shared right-top
           new Point(110, -110, -145), // the right-bottom
           new Point(-88,100,-145)}; // the left-top
   private  Point[] leftBlueStrip ={
           new Point(-70, -110, -140), // the shared left-bottom
           new Point(-55, 100, -140), // the shared right-top
           new Point(-35, 100, -140), // the left-top
           new Point(-50, -110, -140), // the right-bottom

   };
   private  Point[] rightBlueStrip={
           new Point(90, -110, -140), // the shared left-bottom
           new Point(55, 100, -140), // the shared right-top
           new Point(70, -110, -140), // the right-bottom
           new Point(72, 100, -140), // the left-top
   };
   private  Point[] downTriangle={
           new Point(-30, 42, -140), // the  left-top
           new Point(10, -46, -140), // the bottom
           new Point(50, 42, -140), // the right-top
   };
   private  Point[] topTriangle={
           new Point(-32, -21, -140), // the  left-bottom
           new Point(10, 71, -140), // the top
           new Point(53, -21, -140), // the right-bottom
   };
   private  Point[] internalPolygon={
           new Point(3, -17, -139.99), // the bottom left
           new Point(17, -17, -139.99), // the bottom right
           new Point(36, 14, -139.99), // the middle right
           new Point(17, 38, -139.99), // the top right
           new Point(3, 38, -139.99), // the top left
           new Point(-12, 14, -139.99), //  the middle left
   };
   private Geometry sphereLeft = new Sphere(24d, new Point(-52, 95, -140)) //
           .setEmission(new Color(YELLOW).reduce(2)) //
           .setMaterial(material);
   private Geometry sphereRight = new Sphere(24d, new Point(57, 95, -140)) //
           .setEmission(new Color(ORANGE).reduce(2)) //
           .setMaterial(material);
   private Geometry leftCylinder = new Cylinder(new Ray(new Point(-80, 5, -160),new Vector(0.3,1,0.42)),1.5d,100d).setEmission(new Color(black)) //
           .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(60).setKt(0.2));//
   private Geometry rightCylinder = new Cylinder(new Ray(new Point(67, 5, -150),new Vector(-0.1,1,0.32)),1.5d,100d).setEmission(new Color(black)) //
           .setMaterial(new Material().setKd(0.1).setKs(0.1).setShininess(60).setKt(0.2));//
   private Scene scene3 = new Scene("Test scene") //
           .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.005)));
   private Camera camera3 = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
           .setVPSize(200, 200) //
           .setVPDistance(1000).setImageWriter( new ImageWriter("israel", 500 ,500));
   /**
    * Produce a picture of a sphere lighted by a spot light
    */
   @Test
   public void israelTest() {
      scene3.geometries.add(new Polygon(square[3],square[1],square[2],square[0]).setEmission(new Color(WHITE)).setMaterial(material.setKd(0.5).setKs(0.5).setShininess(100))
                      .setMaterial(material),new Polygon(leftBlueStrip[3], leftBlueStrip[2], leftBlueStrip[1], leftBlueStrip[0]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
              new Polygon(rightBlueStrip[3],rightBlueStrip[1],rightBlueStrip[2],rightBlueStrip[0]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
              new Triangle(downTriangle[0],downTriangle[1],downTriangle[2]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
              new Polygon(internalPolygon[0],internalPolygon[1],internalPolygon[2],internalPolygon[3],internalPolygon[4],internalPolygon[5]).setEmission(new Color(white).reduce(2)).setMaterial(material.setKr(0.5)),
              new Triangle(topTriangle[0],topTriangle[1],topTriangle[2]).setEmission(new Color(BLUE).reduce(2)).setMaterial(material),
              sphereLeft,sphereRight,leftCylinder,rightCylinder);
      scene3.lights.add(new PointLight(new Color(WHITE), trDL));
      scene3.lights.add(new SpotLight(trCL, trPL, trDL,0.5).setKl(0.001).setKq(0.001));
      ImageWriter imageWriter = new ImageWriter("israelFlag", 500, 500);
      camera3.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene3)) //
              .renderImage() ; //
      camera3.writeToImage();//
      imageWriter = new ImageWriter("israelFlagRightMoveRotateLeft", 500, 500);
      camera3.moveCamera(new Vector(1100,0,-300));
      camera3.rotateCamera(new Vector(0,1,0),50);
      camera3.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene3)) //
              .renderImage() ; //
      camera3.writeToImage();//
   }



   /**
    * Produce a picture of snow man
    */
   @Test
   public void snowMan() {
      Sphere sphere1 = (Sphere) new Sphere(60, new Point(0,-90,-500))
              .setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setShininess(30));
      sphere1.setEmission(new Color(DARK_GRAY));
      Sphere sphere2 = (Sphere) new Sphere(50, new Point(0,0,-500))
              .setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setShininess(30));
      sphere2.setEmission(new Color(DARK_GRAY));
      Sphere sphere3 = new Sphere(40, new Point(0,70,-500));
      sphere3.setEmission(new Color(DARK_GRAY))
              .setMaterial(new Material().setKd(0.8).setKs(0.2).setKt(0.2).setShininess(30));
      Sphere sphere4 = new Sphere(3, new Point(-12,80,-463));
      sphere4.setEmission(new Color(0,100,0));
      Sphere sphere5 = new Sphere(3, new Point(12,80,-463));
      sphere5.setEmission(new Color(0,100,0));
      Sphere sphere6 = (Sphere) new Sphere(4, new Point(0,0,-450)).setEmission(new Color(BLACK));
      Sphere sphere7 = (Sphere) new Sphere(4, new Point(0,12,-452)).setEmission(new Color(BLACK));
      Sphere sphere8 = (Sphere) new Sphere(4, new Point(0,-12,-452)).setEmission(new Color(BLACK));
      Triangle triangle1 = (Triangle) new Triangle(
              new Point(-3,70,-459),
              new Point(3,70,-459),
              new Point(0,60,-440)
      ).setEmission(new Color(238,125,0));
      Cylinder cylinder1 = (Cylinder) new Cylinder(new Ray(new Point(35,9,-500),new Vector(1,0.7d,1)), 3, 40)
              .setEmission(new Color(50,16,0));
      Cylinder cylinder2 = (Cylinder) new Cylinder(new Ray(new Point(-35,9,-500),new Vector(-1,0.7d,-1)), 3, 40)
              .setEmission(new Color(50,16,0));
      Cylinder cylinder3 = (Cylinder) new Cylinder(new Ray(new Point(90,11,-500),new Vector(-1,0.7d,-1)), 3, 40)
              .setEmission(new Color(50,16,0));
      Cylinder cylinder4 = (Cylinder) new Cylinder(new Ray(new Point(-85,8,-500),new Vector(1,0.7d,1)), 3, 40)
              .setEmission(new Color(50,16,0));

      Triangle polygon1 = (Triangle) new Triangle(
              new Point(-200,-200,-560),
              new Point(200,-200,-560),
              new Point(200,200,-560)
      ).setEmission(new Color(DARK_GRAY)).setMaterial(new Material().setKs(0.8).setShininess(60))//
              ;

      Triangle polygon2 = (Triangle) new Triangle(
              new Point(-200,-200,-560),
              new Point(-200,200,-560),
              new Point(200,200,-560)
      ).setEmission(new Color(DARK_GRAY)).setMaterial(new Material().setKs(0.8).setShininess(60))//
              ;


      Polygon pl1 = (Polygon) new Polygon(
              new Point(-30,-65,-440),
              new Point(30,-65,-440),
              new Point(30,-60,-440),
              new Point(-30,-60,-440))
              .setEmission(new Color(BLUE));

      Polygon pl2 = (Polygon) new Polygon(
              new Point(-30,-105,-440),
              new Point(30,-105,-440),
              new Point(30,-100,-440),
              new Point(-30,-100,-440))
              .setEmission(new Color(BLUE));

      Triangle tr1 = (Triangle) new Triangle(
              new Point(0,-68,-440),
              new Point(-15,-90,-440),
              new Point(15,-90,-440)
      ).setEmission(new Color(BLUE))
              .setMaterial(new Material().setKs(0.8).setShininess(60));

      Triangle tr2 = (Triangle) new Triangle(
              new Point(0,-97,-440),
              new Point(-15,-75,-440),
              new Point(15,-75,-440)
      ).setEmission(new Color(BLUE))
              .setMaterial(new Material().setKs(0.8).setShininess(60));
      ;



      scene2.geometries.add(sphere1, sphere2, sphere3, sphere4, sphere5, triangle1, cylinder1, cylinder2, cylinder3,
              cylinder4, polygon1, polygon2, sphere6, sphere7, sphere8,
              pl1, pl2, tr1, tr2
      );
      //	scene2.lights.add( //
      //			new SpotLight(new Color(WHITE), new Point(0, 100, -450), new Vector(0, 0, -1)) //
      //					.setKl(4E-4).setKq(2E-5));



      scene2.lights.add( //
              new SpotLight(new Color(WHITE), new Point(0, -85, -380), new Vector(0, 0, -2)) //
                      .setKl(0.0004).setKq(0.0000006));
      scene2.lights.add( //
              new SpotLight(new Color(LIGHT_GRAY), new Point(-200, 0, 0), new Vector(1, 1, -2)) //
                      .setKl(0.0004).setKq(0.0000006));
      scene2.lights.add( //
              new PointLight(new Color(GRAY), new Point(-50, 0, 0)));
      scene2.lights.add( //
              new PointLight(new Color(GRAY), new Point(50, 0, 0)));

      scene2.background = new Color(89,108,143);
      ImageWriter imageWriter = new ImageWriter("snowMan", 500, 500);
      camera2.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene2)) //
              .renderImage() ; //
      camera2.writeToImage();
   }

   /**
    * Produce a picture of snow man
    */
   @Test
   public void garden() {

      Cylinder cylinder1 = (Cylinder) new Cylinder(new Ray(new Point(0,-100,-50), new Vector(0,1,0)), 10, 100)
              .setEmission(new Color(50,16,10))
              .setMaterial(new Material().setKd(0.9).setKs(0.1));

      Polygon polygon = (Polygon) new Polygon(
              new Point(-110,-110, -50),
              new Point(110,-110, -50),
              new Point(110,110, -50),
              new Point(-110,110, -50)
      ).setEmission(new Color(186, 255, 255))
              .setMaterial(new Material().setKd(0.9).setKs(0.1));
      scene2.geometries.add(cylinder1, polygon);
      for(int i = 0; i<200; i++){
         Random rand = new Random();
         Point p1 = new Point(rand.nextInt(100) - 50, rand.nextInt(100) - 15 , 0 );
         scene2.geometries.add(new Sphere(rand.nextInt(20)+1, p1).setEmission(new Color(GREEN))
                 .setMaterial(new Material().setKs(0.001).setKd(0.9)));
      }

      for(int i = 0; i<50; i++){
         Random rand = new Random();
         Point p1 = new Point(rand.nextInt(90) - 50, rand.nextInt(90) - 15 , 20 );
         scene2.geometries.add(new Sphere(2, p1).setEmission(new Color(RED)));
      }
      scene2.lights.add( //
              new SpotLight(new Color(WHITE), new Point(0, 500, 700), new Vector(0, -400, -1)) //
                      .setKl(0.0004).setKq(0.0000006));
/*
		scene2.lights.add( //
				new SpotLight(new Color(WHITE), new Point(-200, 0, 0), new Vector(1, 1, -2)) //
						.setKl(0.0004).setKq(0.0000006));
		scene2.lights.add( //
				new PointLight(new Color(WHITE), new Point(0, 0, 10000)));
						*/

      scene2.background = new Color(89,108,143);
      ImageWriter imageWriter = new ImageWriter("garden", 500, 500);
      camera2.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene2)) //
              .renderImage() ; //
      camera2.writeToImage();
   }
}
