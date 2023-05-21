package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {
    /**
     * name of the scene
     */
    public String name;

    /**
     * background color of the scene
     */
    public Color background=Color.BLACK;
    /**
     * Ambient light of the scene
     */
    public AmbientLight ambientLight=AmbientLight.NONE;
    /**
     * Geometries figures of the scene
     */
    public Geometries geometries=new Geometries();
    public List<LightSource> lights = new LinkedList<>();

    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    public Scene(String name)
    {
        this.name=name;
    }

    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    public Scene setGeometries(Geometries geometries)
    {
        this.geometries = geometries;
        return this;
    }
}
