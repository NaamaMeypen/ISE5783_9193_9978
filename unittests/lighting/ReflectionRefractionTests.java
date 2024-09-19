/**
 * 
 */
package lighting;

import static java.awt.Color.*;

import geometries.*;
import org.junit.jupiter.api.Test;

import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import renderer.*;
import scene.Scene;

import java.util.Random;

/** Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 * @author dzilb */
public class ReflectionRefractionTests {
   private Scene scene = new Scene("Test scene");

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   public void twoSpheres() {
      Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
         .setVPSize(150, 150).setVPDistance(1000);

      scene.geometries.add( //
                           new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE)) //
                              .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                           new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED)) //
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
      scene.lights.add( //
                       new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
                          .setKl(0.0004).setKq(0.0000006));

      camera.setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500)) //
         .setRayTracer(new RayTracerBasic(scene)) //
         .renderImage() //
         .writeToImage();
   }

   /** Produce a picture of a sphere lighted by a spot light */
   @Test
   public void twoSpheresOnMirrors() {
      Camera camera = new Camera(new Point(0, 0, 10000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
         .setVPSize(2500, 2500).setVPDistance(10000); //

      scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), 0.1));

      scene.geometries.add( //
                           new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100)) //
                              .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)
                                 .setKt(new Double3(0.5, 0, 0))),
                           new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20)) //
                              .setMaterial(new Material().setKd(0.25).setKs(0.25).setShininess(20)),
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                                        new Point(670, 670, 3000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKr(1)),
                           new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                                        new Point(-1500, -1500, -2000)) //
                              .setEmission(new Color(20, 20, 20)) //
                              .setMaterial(new Material().setKr(new Double3(0.5, 0, 0.4))));

      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
         .setKl(0.00001).setKq(0.000005));

      ImageWriter imageWriter = new ImageWriter("reflectionTwoSpheresMirrored", 500, 500);
      camera.setImageWriter(imageWriter) //
         .setRayTracer(new RayTracerBasic(scene)) //
         .renderImage() //
         .writeToImage();
   }

   /** Produce a picture of a two triangles lighted by a spot light with a
    * partially
    * transparent Sphere producing partial shadow */
   @Test
   public void trianglesTransparentSphere() {
      Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
         .setVPSize(200, 200).setVPDistance(1000);

      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

      scene.geometries.add( //
                           new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                                        new Point(75, 75, -150)) //
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                           new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150)) //
                              .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60)), //
                           new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE)) //
                              .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));

      scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1)) //
         .setKl(4E-5).setKq(2E-7));

      ImageWriter imageWriter = new ImageWriter("refractionShadow", 600, 600);
      camera.setImageWriter(imageWriter) //
         .setRayTracer(new RayTracerBasic(scene)) //
         .renderImage() //
         .writeToImage();
   }


   @Test
   public void myTest() {
      Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1),
              new Vector(0, 1, 0)).setVPSize(200, 200).setVPDistance(1000);

      scene.setAmbientLight(new AmbientLight(new Color(WHITE), 0.15));

      scene.geometries.add(
              new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                      new Point(75, 75, -150)).setMaterial(
                      new Material().setKd(0.5).setKs(0.5).setShininess(60).setKr(0.09)),
              new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140),
                      new Point(75, 75, -150)).setMaterial(
                      new Material().setKd(0.5).setKs(0.5).setShininess(60).setKr(0.09)),
              new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(RED)).setMaterial(
                      new Material().setKd(0.2).setKs(0.2).setShininess(30).setKr(0.5)),
              new Sphere(40d, new Point(-40, -30, -80)).setEmission(new Color(GREEN)).setMaterial(
                      new Material().setKd(0.4).setKs(0.4).setShininess(50).setKt(0.8)),
              new Sphere(20d, new Point(-40, -30, -80)).setEmission(new Color(YELLOW)).setMaterial(
                      new Material().setKd(0.2).setKs(0.2).setShininess(10).setKr(0.8))
      );

      scene.lights.add(new SpotLight(new Color(400, 400, 400), new Point(-40, -30, 0),
              new Vector(0, 0, -1)).setKl(0.0005).setKq(0.0005));
      scene.lights.add(new SpotLight(new Color(800, 800, 800), new Point(60, 50, 0),
              new Vector(0, 0, -1)).setKl(0.0005).setKq(0.0005));
      scene.lights.add(new DirectionalLight(new Color(100, 50, 100), new Vector(-1, -1, -1)));

      ImageWriter imageWriter = new ImageWriter("myTestTrianglesAndSpheres", 600, 600);
      camera.setImageWriter(imageWriter)
              .setRayTracer(new RayTracerBasic(scene))
              .renderImage()
              .writeToImage();
   }
   @Test
   public void specialPicture() {
      Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
              .setVPSize(200, 200).setVPDistance(1000);

      scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

      scene.geometries.add( //
              new Triangle(new Point(-50, -50, 0), new Point(50, -50, 0), new Point(0, 100, 50))
                      .setEmission(new Color(20,20,20)) //
                      .setMaterial(new Material().setKr(0.5)),
              new Triangle(new Point(50, -50, 0), new Point(50, 0, 0), new Point(100, -100, 50))
                      .setEmission(new Color(20,20,20))//
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.6)),
              new Triangle(new Point(-50, -50, 0), new Point(-50, 0, 0), new Point(-100, -100, 50))
                      .setEmission(new Color(20,20,20))//
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.6)),//
              new Sphere(40d, new Point(0, -20, 100)).setEmission(new Color(BLUE)) //
                      .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
              new Sphere(15d, new Point(0, -20, 100)).setEmission(new Color(RED)) //
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));
//                new Sphere(new Point(0, -20, 100), 30d).setEmission(new Color(500,20,35)) //
//                        .setMaterial(new Material().setkD(0.2).setkS(0.2).setnShininess(30).setkT(0.6)));

      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4)) //
              .setKl(0.00001).setKq(0.000005));
      scene.lights.add(new SpotLight(new Color(255, 250, 0), new Point(2,5,2), new Vector(-5, -2, -2)).setNarrowBeam(10).setKl(0.0001).setKq(0.00004));
      scene.lights.add(new SpotLight(new Color(0, 0, 255), new Point(2,5,2), new Vector(10, 2, -2)).setNarrowBeam(10).setKl(0.0001).setKq(0.00004));
      scene.lights.add(new SpotLight(new Color(0, 255, 0), new Point(2,5,2), new Vector(10, -2, -2)).setNarrowBeam(10).setKl(0.0001).setKq(0.00004));
      scene.lights.add(new PointLight(new Color(800, 500, 250), new Point(30, -10, -100)).setKl(0.001).setKq(0.0002));

      ImageWriter imageWriter = new ImageWriter("special picture", 600, 600);
      camera.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene)) //
              .renderImage() //
              .writeToImage();
   }
   @Test
   public void specialPicture2() {
      Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0))
              .setVPSize(200, 200).setVPDistance(1000);

      scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

      scene.geometries.add(new Triangle(new Point(-50, -50, 0), new Point(50, -50, 0), new Point(0, 100, 50))
                      .setEmission(new Color(20,20,20))
                      .setMaterial(new Material().setKr(0.5)),
              new Triangle(new Point(50, -50, 0), new Point(50, 0, 0), new Point(100, -100, 50))
                      .setEmission(new Color(20,20,20))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.6)),
              new Triangle(new Point(-50, -50, 0), new Point(-50, 0, 0), new Point(-100, -100, 50))
                      .setEmission(new Color(20,20,20))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.6)),
              new Sphere(40d, new Point(0, -20, 100)).setEmission(new Color(BLUE))
                      .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
              new Sphere(15d, new Point(0, -20, 100)).setEmission(new Color(RED))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)),
              new Tube(new Ray(new Point(0, -50, 0), new Vector(0, 1, 0)), 50)
                      .setEmission(new Color(GREEN)).setMaterial(new Material().setKd(0.6).setKs(0.4)
                              .setShininess(80).setKt(0.8)));


      scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
              .setKl(0.00001).setKq(0.000005));
      scene.lights.add(new SpotLight(new Color(255, 250, 0), new Point(2,5,2), new Vector(-5, -2, -2)).setNarrowBeam(10).setKl(0.0001).setKq(0.00004));


      ImageWriter imageWriter = new ImageWriter("special picture 2", 600, 600);
      camera.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene)) //
              .renderImage() //
              .writeToImage();
   }

   @Test
   public void pyramidTransparentSphere() {
      Camera camera = new Camera(new Point(-140, 20, 35), new Vector(1, -0.15, -0.25), new Vector(1, 0, 4))
              .setVPSize(200, 200).setVPDistance(1000);

      scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));

      scene.geometries.add( //
              new Plane(new Point(10, 0, -30), new Point(0, 10, -30), new Point(-10, 0, -30))
                      .setEmission(new Color(190,0,190)).setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0).setKr(1)),
              new Polygon(new Point(10, 0, -10), new Point(0, 10, -10), new Point(-10, 0, -10),
                      new Point(0, -10, -10))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.2).setKr(0)),
              new Polygon(new Point(10, 0, -10), new Point(0, -10, -10), new Point(0, -10, 0),
                      new Point(10, 0, 0))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0).setKr(1)),
              new Polygon(new Point(10, 0, -10), new Point(0, 10, -10), new Point(0, 10, 0),
                      new Point(10, 0, 0))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.4)),
              new Polygon(new Point(-10, 0, -10), new Point(0, 10, -10), new Point(0, 10, 0),
                      new Point(-10, 0, 0))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.8)),
              new Polygon(new Point(-10, 0, -10), new Point(0, -10, -10), new Point(0, -10, 0),
                      new Point(-10, 0, 0))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.4)),
              new Polygon(new Point(10, 0, 0), new Point(0, -10, 0), new Point(-10, 0, 0),
                      new Point(0, 10, 0))
                      .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.5)),
              new Triangle(new Point(10, 0, 0), new Point(0, -10, 0), new Point(0, 0, 10)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKt(0.5).setKr(0)), //
              new Triangle(new Point(10, 0, 0), new Point(0, 10, 0), new Point(0, 0, 10)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKt(1)), //
              new Triangle(new Point(-10, 0, 0), new Point(0, 10, 0), new Point(0, 0, 10)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKt(0.8)), //
              new Triangle(new Point(-10, 0, 0), new Point(0, -10, 0), new Point(0, 0, 10)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(60).setKt(1)), //
              new Sphere( 2,new Point(0, 0, 3)) //
                      .setEmission(new Color(java.awt.Color.BLUE)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)),
              new Sphere(2,new Point(3, 0, -6)) //
                      .setEmission(new Color(java.awt.Color.RED)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0).setKr(1)),
              new Sphere(2,new Point(-3, 3, -6) ) //
                      .setEmission(new Color(java.awt.Color.GREEN)) //
                      .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)));

      scene.lights.add(new SpotLight(new Color(700, 400, 400), new Point(30, 30, 100), new Vector(0, 0, -1)) //
              .setKl(4E-5).setKq(2E-7));

      ImageWriter imageWriter = new ImageWriter("refractionShadowPyramid", 600, 600);
      camera.setImageWriter(imageWriter) //
              .setRayTracer(new RayTracerBasic(scene)) //
              .renderImage(); //
      camera.writeToImage();
   }
   }
