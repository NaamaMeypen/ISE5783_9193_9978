package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera {
    /**
     * right direction vector from camera
     */
    private Vector vRight;
    /**
     * forward direction vector from camera
     */
    private Vector vTo;
    /**
     * up direction vector from camera
     */
    private Vector vUp;
    /**
     * Camera Position
     */
    private Point p0;
    /**
     * distance from camera to the view pane
     */
    private double distance;
    /**
     * width of view plane
     */
    private double width;
    /**
     * height of view plane
     */
    private double height;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    public Camera(Point p0,Vector vTo,Vector vUp)
    {
        if(!isZero(vTo.dotProduct(vUp)))
        {
            throw new IllegalArgumentException("The vectors _vUp and _vTo are not perpendicular");
        }
        this.p0 = p0;
        this.vTo=vTo.normalize();
        this.vUp=vUp.normalize();
        this.vRight=vTo.crossProduct(vUp);
    }
    public Camera setVPSize(double width, double height)
    {
        this.width=width;
        this.height=height;
       return this;
    }
    public Camera setVPDistance(double distance)
    {
        this.distance=distance;
        return this;
    }

    public Camera setWidth(double width) {
        this.width = width;
        return this;
    }

    public Camera setHeight(double height) {
        this.height = height;
        return this;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public Ray constructRay(int nX, int nY, double j, double i)
    {
        //Image center
       Point Pc= p0.add(vTo.scale(distance));
       //Ratio (pixel width & height)
       double Ry=alignZero(height/nY);
       double Rx=alignZero(width/nX);
       //Pixel[i,j] center
        double Yi = alignZero(-(i-(nY-1)/2d)*Ry);
        double Xj = alignZero((j-(nX-1)/2d)*Rx);
       Point Pij = Pc;
        //check if Xj or Yi are on the pivot
        if(!isZero(Xj))
            Pij=Pij.add(vRight.scale(Xj));
        if(!isZero(Yi))
            Pij=Pij.add(vUp.scale(Yi));
        return new Ray(p0,Pij.subtract(p0).normalize());

    }

    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }

    public Camera setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }
    public Camera renderImage()
    {
        try {
            if (imageWriter == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (this == null) {
                throw new MissingResourceException("missing resource", Camera.class.getName(), "");
            }
            if (rayTracer == null) {
                throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
            }
            //rendering the image
            int nX = imageWriter.getNx();
            int nY = imageWriter.getNy();
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    Color color = castRay(nX, nY, j, i);
                    imageWriter.writePixel(j, i, color);
                }
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
        return this;
    }
    public void printGrid(int interval, Color color)
    {
        if (imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nX; i++)
            for (int j = 0; j < nY; j++)
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
        imageWriter.writeToImage();
    }
    public void writeToImage() {
        if(imageWriter==null)
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        imageWriter.writeToImage();
    }
    private Color castRay(int nX, int nY, double j, double i)
    {

        Ray ray=constructRay(nX,nY,j,i);
        return rayTracer.traceRay(ray);
    }

    /**
     * Rotate camera through axis and angle of rotation
     * @param axis Axis of rotation
     * @param theta Angle of rotation (degrees)
     */
    public void rotateCamera(Vector axis, double theta)
    {
        //rotate all vector's using Vector.rotateVector Method
        if (theta == 0) return; //no rotation
        vUp = vUp.rotateVector(axis, theta);
        vRight = vRight.rotateVector(axis, theta);
        vTo = vTo.rotateVector(axis, theta);
    }

    /**
     * Move camera (move point of view of the camera)
     * @param move {@link Vector} Vertical distance
     */
    public void moveCamera(Vector move) {
        //move Point0 according to params
        Point myPoint = new Point(p0.getXyz());
        myPoint=  myPoint.add(move);
        p0 = myPoint;
    }
}

