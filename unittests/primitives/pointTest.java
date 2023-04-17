package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for primitives.point class
 * @author Naama Meypen && Odelya Houri
 */
class pointTest {

    /**
     * Test method for {@link point#subtract}
     */
    @Test
    void testSubtract() {
        point p=new point(1,1,1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(p, new point(2, 3, 4).subtract(new point(1, 2, 3)), //
                "Wrong point subtract");

        // =============== Boundary Values Tests ==================
        // TC11: test subtracting same point
        assertThrows(IllegalArgumentException.class, () -> p.subtract(p), //
                "Subtract P from P must throw exception");
    }


    /**
     * Test method for {@link primitives.point#add}.
     */
    @Test
    void testAdd() {
        point p=new point(1,1,1);
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(new point(2, 3, 4),p.add(new Vector(1, 2, 3)), //
                "Wrong point add");


        // =============== Boundary Values Tests ==================
        // there are no boundary tests
    }



    /**
     * Test method for {@link point#distanceSquared}
     */
    @Test
    void testDistanceSquared() {


        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(14d, new point(1, 1, 1).distanceSquared(new point(2, 3, 4)), 0.0001, //
                "Wrong squared distance between the point and itself");

        // =============== Boundary Values Tests ==================
        // TC11: test distance with the same point
        assertEquals(0d, new point(1, 2, 3).distanceSquared(new point(1, 2, 3)), 0.0001, //
                "Wrong squared distance between the point and itself");

    }

    /**
     * Test method for {@link point#distance}
     */
    @Test
    void testDistance() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(Math.sqrt(14), new point(1, 1, 1).distance(new point(2, 3, 4)), 0.0001, //
                "Wrong distance between the point and itself");

        // =============== Boundary Values Tests ==================
        // TC11: test distance with the same point
        assertEquals(0d, new point(1, 2, 3).distance(new point(1, 2, 3)), 0.0001, //
                "Wrong distance between the point and itself");

    }
}