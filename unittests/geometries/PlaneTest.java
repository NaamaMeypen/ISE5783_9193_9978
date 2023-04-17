package geometries;

import org.junit.jupiter.api.Test;
import primitives.Vector;
import primitives.point;

import static org.junit.jupiter.api.Assertions.*;

class PlaneTest {
    void testConstructor()
    {
        // =============== Boundary Values Tests ==================
        // TC10: The first and second points are connected
        assertThrows(IllegalArgumentException.class,()->new Plane(new point(1,2,3),new point(1,2,3),
                new point(1,1,1)),"\"Construct a plan with the first and second points are converge" +
                " must throw an exeption\"");
         //TC20: three points that are on the same line
        assertThrows(IllegalArgumentException.class, ()->new Plane(new point(1,2,3),new point(2,4,6),
                new point(3,6,9)),
                "Construct a plan withthree points that are on the same line must throw an exception\"");
        // ============ Equivalence Partitions Tests ==============
        Plane p = new Plane(new point(1,2,3),new point(1,2,3),new point(1,1,1));


    }

    /**
     * Test method for {@link Plane#getNormal}
     */
    @Test
    void testGetNormal() {
        point p1 = new point(1,2,0);
        point p2 = new point(4,-9,0);
        point p3 = new point(1,0,8);
        Plane p = new Plane(p1, p2, p3);
        Vector v1 = p1.subtract(p2);
        Vector v2 = p2.subtract(p3);
        Vector v3 = p3.subtract(p1);
        Vector n = p.getNormal();

        assertEquals(0d,v1.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals(0d,v2.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals(0d,v2.dotProduct(n),0.0001 ,"ERROR: incorrect normal for plane\n");
        assertEquals( 1d, n.lengthSquared(), 0.00001,"The normal have to be normalized\n");
    }
}