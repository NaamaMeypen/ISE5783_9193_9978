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
    private RayTracerBase rayTracerBase;
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

    public Ray constructRayThroughPixel(int nX, int nY, int j, int i)
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

    public Camera setRayTracerBase(RayTracerBase rayTracerBase) {
        this.rayTracerBase = rayTracerBase;
        return this;
    }
    void renderImage()
    {
        try {
            if (imageWriter == null) {
                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
            }
            if (this == null) {
                throw new MissingResourceException("missing resource", Camera.class.getName(), "");
            }
            if (rayTracerBase == null) {
                throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
            }
            //rendering the image
            int nX = imageWriter.getNx();
            int nY = imageWriter.getNy();
            for (int i = 0; i < nY; i++) {
                for (int j = 0; j < nX; j++) {
                    imageWriter.writePixel(j, i, castRay(nX, nY, j, i));
                }
            }
        } catch (MissingResourceException e) {
            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
        }
    }
    public void printGrid(int interval, Color color)
    {
        if (imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        for (int i = 0; i < imageWriter.getNx(); i++)
            for (int j = 0; j < imageWriter.getNy(); j++)
                if (i % interval == 0 || j % interval == 0)
                    imageWriter.writePixel(j, i, color);
        imageWriter.writeToImage();
    }
    public void writeToImage() {
        if(imageWriter==null)
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        imageWriter.writeToImage();
    }
    private Color castRay(int nX, int nY, double j, double i) {

        //Pc = P0 + d * vTo
        Point pc = p0.add(vTo.scale(distance));
        Point pIJ = pc;

        //Ry = height / nY : height of a pixel
        double rY = alignZero(height / nY);
        //Ry = weight / nX : width of a pixel
        double rX = alignZero(width / nX);
        //xJ is the value of width we need to move from center to get to the point
        double xJ = alignZero((j - ((nX - 1) / 2d)) * rX);
        //yI is the value of height we need to move from center to get to the point
        double yI = alignZero(-(i - ((nY - 1) / 2d)) * rY);

        if (xJ != 0) {
            pIJ = pIJ.add(vRight.scale(xJ)); // move to the point
        }
        if (yI != 0) {
            pIJ = pIJ.add(vUp.scale(yI)); // move to the point
        }

        //get vector from camera p0 to the point
        Vector vIJ = pIJ.subtract(p0);

        //return ray to the center of the pixel
        Ray myRay = new Ray(p0, vIJ);
        return rayTracerBase.traceRay(myRay);
    }
}

