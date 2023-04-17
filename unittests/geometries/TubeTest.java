package geometries;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import primitives.point;
import primitives.Vector;
import primitives.Ray;
class TubeTest {

    /**
     * Test method for {@link Tube#getNormal}
     */
    @Test
    void testGetNormal() {

//        try{
//            assertThrows(IllegalArgumentException.class,()->)
//        }

        Tube tube= new Tube(new Ray(new point(0,0,0), new Vector(0,0,1)), 1 );
        point pt1= new point(0,1,2);
        assertEquals(new Vector(0,1,0), tube.getNormal(pt1), "that is not a tube proper normal vector!");


        //---------------------BVA if the ankle between the point and the start of ray do a square-------------
        point pt2 = new point(0,1,0);
        assertEquals(new Vector(0,1,0), tube.getNormal(pt2), "that is not a tube proper normal vector!");
        //---------------------BVA if the ankle between the point and the start of ray do a square-------------
        point pt3 = new point(0,0,1);
        assertThrows(IllegalArgumentException.class, () -> tube.getNormal(pt3),
                "getNormal() for parallel inner point does not throw an exception");
        }
}