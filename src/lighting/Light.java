package lighting;

import primitives.Color;

abstract class Light {
    final private Color intensity;

    protected Light(Color intensity) {
        this.intensity = intensity;
    }
    /**
     * getter for intensity
     * @return intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
