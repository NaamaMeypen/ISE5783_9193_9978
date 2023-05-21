package geometries;

import org.junit.jupiter.api.Test;
import primitives.*;
import geometries.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SphereTest {

    /**
     * Test method for {@link Sphere#getNormal}
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Sphere s =new Sphere(1d,new Point(0,0,1));
        assertDoesNotThrow( () ->s.getNormal(new Point(0, 0, 2)), "");
        //TC02:
//        assertThrows(IllegalArgumentException.class,() ->s.getNormal(new Point(0,0,3)),
//                "getNormal with a point that isn't on the sphere must throw an exception\n");

    }
    /** Test method for {@link Sphere#findIntersections} */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, new Point (1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                "Ray's line out of sphere");
        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-1, 0, 0),
                new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");
        // TC03: Ray starts inside the sphere (1 point)
        p1 = new Point(1.714165074248383,0.6999773187209722,0);
        result = sphere.findIntersections(new Ray(new Point(1.48,0,0),new Vector(0.93,2.78,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses sphere");
        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1.35,2.05,0), new Vector(0.39,1.86,0))),
                "Ray's line out of sphere");
        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 points)
        p1 = new Point(0.1870079690895916,0.5822748128471384,0);
        result = sphere.findIntersections(new Ray(new Point(2,0,0),new Vector(-2.74,0.88,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray start on sphere");
        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,0), new Vector(0.16,0.17,0))),
                "Ray start on sphere");
        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        p2 = new Point(1.1010898981170623,-0.9820340882121437,-0.1593420286303212);
        p1 = new Point(0.9073990487991217,0.9832194073064717,0.1571771641575327);
        result = sphere.findIntersections(new Ray(new Point(1.23, -2.29, -0.37),
                new Vector(-0.41,4.16,0.67)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere and goes through the center");
        // TC14: Ray starts at sphere and goes inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(1.1,-0.98,-0.16),new Vector(-0.32,3.26,0.53)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(0.9073227588225634,0.9828993944951359,0.15912168070012947)), result, "Ray start on sphere and goes through the center");
        // TC15: Ray starts inside (1 point)
       result = sphere.findIntersections(new Ray(new Point(1.5,0,0), new Vector(1,0,0)));
       assertEquals(1, result.size(), "Wrong number of points");
       assertEquals(List.of(new Point(2,0,0)), result, "Bad intersection point");
        // TC16: Ray starts at the center (1 point)
        result = sphere.findIntersections(new Ray(new Point(1,0,0), new Vector(1,0,0)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(new Point(2,0,0)), result, "Bad intersection point");
        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(2,0,0), new Vector(1,0,0))),
                "Ray start at sphere");
        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3,0,0), new Vector(1,0,0))),
                "Ray start after sphere");
        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0,1,0), new Vector(1,0,0))),
                "Ray starts before the tangent point");
        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(1,1,0), new Vector(1,0,0))),
                "Ray starts at the tangent point");
        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2,1,0), new Vector(1,0,0))),
                "Ray starts after the tangent point");
        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(-0.5,0,0), new Vector(0,1,0))),
                "Ray's line is outside, ray is orthogonal to ray start to sphere's center line");
    }

}