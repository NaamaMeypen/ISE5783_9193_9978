package renderer;

import geometries.Intersectable;
import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraIntegrationTest {
    /**
     * help function that get the camera, an intersectable geometry and an expected num of
     * intersection between all the rays throw all the piksels throw the view plane
     * and the geometry.
     */
    private void assertCountIntersections (int expected,Camera cam, Intersectable geo,int Nx,int Ny)
    {
        int count =0;
        List<Point> allpoints = null;
        for (int i = 0; i < Ny; ++i) {
            for (int j = 0; j < Nx; ++j) {
                var intersections = geo.findIntersections(cam.constructRayThroughPixel(Nx, Ny, j, i));
                if (intersections != null) {
                    if (allpoints == null) {
                        allpoints = new LinkedList<>();
                    }
                    allpoints.addAll(intersections);
                }
                count += intersections == null ? 0 : intersections.size();
            }
        }
        assertEquals(expected, count, "Wrong amount of intersections");
    }
    /**
   * Integration tests of Camera Ray construction with Ray-Sphere intersections
     */
    @Test
  public void cameraRaySphereIntegration()
    {
        Camera cam1 = new Camera(new Point(0,0,0), new Vector(0, 0, -1), new Vector(0, -1, 0));
        Camera cam2 = new Camera(new Point(0, 0, 0.5), new Vector(0, 0, -1), new Vector(0, -1, 0));

        // TC01: Small Sphere 2 points
        assertCountIntersections(1,cam1, new Sphere(1, new Point(0, 0, -3)),3,3 );

        // TC02: Big Sphere 18 points
        assertCountIntersections(18,cam2, new Sphere(2.5, new Point(0, 0, -2.5)),3,3);

        // TC03: Medium Sphere 10 points
        assertCountIntersections(10,cam2, new Sphere(2, new Point(0, 0, -2)), 3,3);

        // TC04: Inside Sphere 9 points
        assertCountIntersections(9,cam2, new Sphere(4, new Point(0, 0, -1)), 3,3);

        // TC05: Beyond Sphere 0 points
        assertCountIntersections(0,cam1, new Sphere(0.5, new Point(0, 0, 1)), 3,3);
    }
    /**
     * Integration tests of Camera Ray construction with Ray-Plane intersections
     */
   @Test
   public void cameraRayPlaneIntegration()
   {
        Camera cam = new Camera(new Point(0,0,0), new Vector(0, 0, -1), new Vector(0, -1, 0));

        // TC01: Plane against camera 9 points
        assertCountIntersections(9,cam, new Plane(new Point(0, 0, -5), new Vector(0, 0, 1)), 3,3);

        // TC02: Plane with small angle 9 points
        assertCountIntersections(9,cam, new Plane(new Point(0, 0, -5), new Vector(0, 1, 2)),3,3 );

        // TC03: Plane parallel to lower rays 6 points
        assertCountIntersections(6,cam, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)),3,3 );

        // TC04: Beyond Plane 0 points
        assertCountIntersections(6,cam, new Plane(new Point(0, 0, -5), new Vector(0, 1, 1)),3,3 );
   }
    /**
     * Integration tests of Camera Ray construction with Ray-Triangle intersections
     */
    @Test
    public void cameraRayTriangleIntegration()
    {
        Camera cam = new Camera(new Point(0,0,0), new Vector(0, 0, -1), new Vector(0, -1, 0));
        // TC01: Small triangle 1 point

        assertCountIntersections(1,cam, new Triangle(new Point(1, 1, -2), new Point(-1, 1, -2), new Point(0, -1, -2)), 3,3);

        // TC02: Medium triangle 2 points
        assertCountIntersections(2,cam, new Triangle(new Point(1, 1, -2), new Point(-1, 1, -2), new Point(0, -20, -2)), 3,3);
    }


}
