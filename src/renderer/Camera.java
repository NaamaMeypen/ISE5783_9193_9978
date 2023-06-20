package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.stream.IntStream;

import static primitives.Util.*;

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
    private boolean isAdaptiveSuperSamplingOn = false;


    private boolean isAntiAliasingOn=false;

    private int numOfRays=300;
    private int adaptiveMaxDepth = 4;
    private boolean isMultithreadingOn=false;
    int numOfThreads=4;

    public Camera setNumOfThreads(int numOfThreads) {
        this.numOfThreads = numOfThreads;
        return this;
    }

    public Camera setAdaptiveSuperSamplingOn(boolean adaptiveSuperSamplingOn) {
        isAdaptiveSuperSamplingOn = adaptiveSuperSamplingOn;
        return this;
    }
    /** Setter of builder patters
     * sets the multithreading
     * If set to 1, the render won't use the thread pool.
     * If set to 0, the thread pool will pick the number of threads.
     *
     * @param threads number of threads to use
     * @return the current render
     * @throws IllegalArgumentException when threads is less than 0
     */
    public Camera setMultithreadingOn(int threads) {
        if (threads < 0)//threads cannot be less than zero
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");

        isMultithreadingOn = true;

        if (threads != 0)//any number that is not zero is acceptable
            numOfThreads = threads;

        else {//if number received was zero:
            int cores = Runtime.getRuntime().availableProcessors() - 2; //leave 2 spare threads
            numOfThreads = cores <= 2 ? 1 : cores;//if cores is smaller than 2 than threads is 1, i=otherwise it is equal to cores
        }
        return this;
    }
    public Camera setMultithreadingOn(boolean multithreadingOn) {
        isMultithreadingOn = multithreadingOn;
        return this;
    }

    public Camera setAntiAliasingOn(boolean antiAliasingOn) {
        isAntiAliasingOn = antiAliasingOn;
        return this;
    }
    public Camera setNumOfRays(int numOfRays) {
        this.numOfRays = numOfRays;
        return this;
    }

    public Camera(Point p0, Vector vTo, Vector vUp)
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

    /**
     * > The function calculates the center point of the pixel in the 3D space
     *
     * @param nX number of pixels in the width of the image
     * @param nY number of pixels in the vertical direction
     * @param j  the column number of the pixel in the image
     * @param i  the row number of the pixel in the image
     * @return the point in the middle of the screen.
     */
    private Point CalculatCenterPointInPixel(int nX, int nY, double j, double i) {
        // Finding the point that is distance units away from the position in the direction of the vector v1.
        Point pC = p0.add(vTo.scale(distance));

        // Calculating the width and height of each cell in the grid.
        double rY = height / nY;
        double rX = width / nX;

        // Calculating the y and x coordinate of the pixel in the image.
        double yI = -(i - (nY - 1) / 2d) * rY;
        double xJ = (j - (nX - 1) / 2d) * rX;
        // Creating a new Point object and assigning it to pIJ.
        Point pIJ = pC;
        // Checking if the value of xJ is not equal to zero.
        if (!isZero(xJ)) {
            // Adding the vector v3 to the vector pIJ.
            pIJ = pIJ.add(vRight.scale(xJ));
        }

        // Checking if the value of yI is not equal to zero.
        if (!isZero(yI)) {
            // Adding the vector v2 to the vector pIJ.
            pIJ = pIJ.add(vUp.scale(yI));
        }

        // Creating a new array of size n and filling it with the values of the array pIJ.
        return pIJ;
    }
    
    public Ray constructRay(int nX, int nY, double j, double i)
    {

        Point Pij=CalculatCenterPointInPixel(nX,nY,j,i);
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

    /**
     * Find a ray from p0 to the center of the pixel from the given resolution.
     * @return ray from p0 the center to the center of the pixel in row and column
     * @throws MissingResourceException if the camera is not initialized
     */
    public Camera renderImage() throws MissingResourceException {
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        // if one of the fields hasn't been initialized throw an exception
        if (imageWriter == null) {
            throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
        }
        if (rayTracer == null) {
            throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
        }
        if(isMultithreadingOn) {
            IntStream.range(0,nX).parallel().forEach(row -> {
                IntStream.range(0, nY).parallel().forEach(column -> {
                    Color color = castRay(nX, nY, row, column);
                    imageWriter.writePixel(row, column, color);
                });
            });
        }
        else
        {
            for (int i = 0; i < nX; i++)
            {
                for (int j = 0; j < nY; j++)
                {
                    Color color = castRay(nX, nY, j, i);
                    imageWriter.writePixel(j, i, color);
                }
            }
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
    /**
     * This function implements adaptive super-sampling
     * @param nX
     * @param nY
     * @param j
     * @param i
     * @return
     */
    private Color adaptiveAntiAliasing(int nX, int nY, double j, double i) {
        Point center = CalculatCenterPointInPixel(nX,nY,j,i);
        double pixelSize = this.height/nY;
        return adaptiveAntiAliasingRec(center,pixelSize, adaptiveMaxDepth);
    }

    /**
     * This is the recursive function for adaptive super sampling
     * @param center
     * @param squareSize
     * @param depth
     * @return
     */
    private Color adaptiveAntiAliasingRec(Point center, double squareSize, int depth){
        Color result = Color.BLACK;
        Point newCenter = null;
        ArrayList<Color> quartersColors = new ArrayList<Color>(); // each time we divide the square to four squares
        Point p1 = center.add(vUp.scale(0.5*squareSize)).add(vRight.scale(-0.5*squareSize));
        Point p2 = center.add(vUp.scale(0.5*squareSize)).add(vRight.scale(0.5*squareSize));
        Point p3 = center.add(vUp.scale(-0.5*squareSize)).add(vRight.scale(-0.5*squareSize));
        Point p4 = center.add(vUp.scale(-0.5*squareSize)).add(vRight.scale(0.5*squareSize));
        Color p1Color = rayTracer.traceRay(new Ray(p0, p1.subtract(p0)));
        Color p2Color = rayTracer.traceRay(new Ray(p0, p2.subtract(p0)));
        Color p3Color = rayTracer.traceRay(new Ray(p0, p3.subtract(p0)));
        Color p4Color = rayTracer.traceRay(new Ray(p0, p4.subtract(p0)));
        Color centerColor = rayTracer.traceRay(new Ray(p0, center.subtract(p0)));
        if(depth == 0 || p1Color.equals(p2Color) && p2Color.equals(p3Color)
                && p3Color.equals(p4Color) && p4Color.equals(centerColor)){
            return p1Color.add(p2Color,p3Color,p4Color,centerColor).scale(0.20);
        }
        else{
            newCenter = center.add(vRight.scale(-0.25*squareSize)).add(vUp.scale(0.25*squareSize));
            result = result.add(adaptiveAntiAliasingRec(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(0.25*squareSize)).add(vUp.scale(0.25*squareSize));
            result = result.add(adaptiveAntiAliasingRec(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(-0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasingRec(newCenter,0.5*squareSize,depth - 1));

            newCenter = p4.add(vRight.scale(0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasingRec(newCenter,0.5*squareSize,depth - 1));

            return result.scale(0.25);
        }
    }

    private Color castRay(int nX, int nY, double j, double i)
    {
        if(isAdaptiveSuperSamplingOn)
            return  adaptiveAntiAliasing(nX,nY,j,i);
        if (isAntiAliasingOn)
            return antiAliasing(nX, nY, j, i);

        Ray ray = constructRay(nX, nY, j, i);
        return rayTracer.traceRay(ray);
    }

    private Color antiAliasing(int nX, int nY, double j, double i) {
        List<Ray> rays = constructRays(nX, nY, j, i);
        Color color =Color.BLACK;
        for (Ray ray:rays)
        {
            Color color1 = rayTracer.traceRay(ray);
            color=color.add(color1);
        }
        return color.reduce(numOfRays);
    }

    private List<Ray> constructRays(int nX, int nY, double j, double i) {
        List<Ray> rays=new ArrayList<>(numOfRays+1);

        Point pInPixel;

        // Calculating the width and height of each cell in the grid
        double rX = width / nX;
        double rY = height / nY;

        // Declaring a variable of type double called randX and randY.
        double randX, randY;
        // Calculating the center point of the pixel in the image.
        Point pIJ = CalculatCenterPointInPixel(nX, nY, j, i);
        //adding the original ray to the rays list
        rays.add(constructRay(nX,nY,j,i));
        double x=pIJ.getX();
        double y=pIJ.getY();
        double z =pIJ.getZ();
        for (int k = 0; k < numOfRays; k++) {

            // Generating a random number between -rX/2 and rX/2.
            randX = random(-rX / 2, rX / 2);

            // Generating a random number between -rY/2 and rY/2.
            randY = random(-rY / 2, rY / 2);

            // Creating a new point that is a random distance from the center point.
            pInPixel = new Point(x + randX, y + randY, z);

            // Creating a ray from the camera to the pixel.
            rays.add(new Ray(p0, pInPixel.subtract(p0)));
        }
            return rays;
        //return List.of(constructRay(nX,nY,j,i));
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
        p0 = p0.add(move);
    }

}

