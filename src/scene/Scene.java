package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

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

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
}
