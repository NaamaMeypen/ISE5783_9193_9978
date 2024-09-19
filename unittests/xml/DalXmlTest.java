/*
package xml;

import org.junit.jupiter.api.Test;
import primitives.Color;
import primitives.Point;
import primitives.Vector;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import scene.Scene;

import javax.xml.parsers.ParserConfigurationException;

import static java.awt.Color.YELLOW;
import static org.junit.jupiter.api.Assertions.*;

class DalXmlTest {

    */
/**
     * Test method for {@link DalXml#getSceneFromXML}
     *//*

    @Test
    void testGetSceneFromXML() {
    }
    */
/**
     * Test for XML based scene - for bonus
     *//*

    @Test
    public void basicRenderXml() throws ParserConfigurationException {
        DalXml xml = new DalXml("images/basicRenderTestTwoColors");
        Scene scene = xml.getSceneFromXML();

        // enter XML file name and parse from XML file into scene object
        // ...

        Camera camera = new Camera(Point.ZERO, new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPDistance(100) //
                .setVPSize(500, 500)
                .setImageWriter(new ImageWriter("xml render test", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene));
        camera.renderImage();
        camera.printGrid(100, new Color(java.awt.Color.YELLOW));
        camera.writeToImage();
    }
}*/
