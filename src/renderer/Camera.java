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
    private boolean isAdaptiveSuperSamplingOn = true;
    private boolean isAntiAliasingOn=true;

    private int numOfRays=300;
    private int adaptiveMaxDepth = 4;
    private boolean isMultithreading=true;
    int numOfThreads=30;

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

    /*     //Image center
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
              Pij=Pij.add(vUp.scale(Yi));*/
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
/*
    *//* function that get the color of each point in
     * the view plane and paint it . *//*
   public Camera renderImage() {
       // if one of the fields hasn't been initialized throw an exception
       if (imageWriter == null) {
           throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
       }
       if (rayTracer == null) {
           throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
       }

       int nX = imageWriter.getNx();
       int nY = imageWriter.getNy();
        //if multithreading is set, render image with multithreads
        if (isMultithreading) {
            Pixel.initialize(nX, nY, 5);

            Pixel p = new Pixel();
            //create thread foreach ray calculation
            Thread[] threads = new Thread[numOfThreads];
            for (int i = numOfThreads - 1; i >= 0; --i) {
                threads[i] = new Thread(() -> {
                    while (p.nextPixel()) {
                        Color color=castRay(nX, nY, p.col, p.row);
                        imageWriter.writePixel( p.col, p.row, color);
                       // renderHelper(nX, nY, p.col, p.row);
                        Pixel.pixelDone();
                    }
                });
            }
            // Start threads
            for (Thread thread : threads) thread.start();
            Pixel.waitToFinish();
            // Wait for all threads to finish
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (Exception e) {
                }
            }
        }
        else {
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
    }*/
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
        if(isMultithreading) {
            IntStream.range(0, imageWriter.getNx()).parallel().forEach(row -> {
                IntStream.range(0, imageWriter.getNy()).parallel().forEach(column -> {
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
        /*
        //if anti aliasing is not set, construct one ray per pixel
        else if (!isAntiAliasingOn) {
            //go over all the pixels
            for (int i = 0; i < nX; i++) {
                for (int j = 0; j < nY; j++) {
                    // construct a ray through the current pixel
                    Ray ray = this.constructRay(nX, nY, j, i);
                    // get the  color of the point from trace ray
                    Color color = rayTracer.traceRay(ray);
                    // write the pixel color to the image
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
                //otherwise, do antialiasing without adaptive
        else if (!isAdaptiveSuperSamplingOn) {
            //go over all the pixels
            for (int i = 0; i < nX; i++) {
                for (int j = 0; j < nY; j++) {
                    // construct a ray through the current pixel
                    List<Ray> rays = this.constructAntiAliasingRays(nX, nY, j, i);
                    // get the  color of the point from trace ray
                    Color color = rayTracer.traceRays(rays);
                    // write the pixel color to the image
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        //otherwise use adaptive supersampling
        else {
            //go over all the pixels
            for (int i = 0; i < nX; i++) {
                for (int j = 0; j < nY; j++) {
                    // construct a ray through the current pixel
                    List<Ray> rays = this.constructAntiAliasingRays(nX, nY, j, i);
                    // get the  color of the point from trace ray
                    Color color = rayTracer.adaptiveTraceRays(rays);
                    // write the pixel color to the image
                    imageWriter.writePixel(j, i, color);
                }
            }
        }*/


/*    public Camera renderImage()
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
    }*/

/*    *//**
     * this method render a 3d scene to an image.
     *//*
    public Camera renderImage() {
        if(imageWriter == null ||
                rayTracer == null)
            throw new MissingResourceException("Not all fields of camera were initiallized", "", "");
        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();

        if(isMultithreadingOn){
            Pixel.initialize(nY, nX, getPrintInterval());
            IntStream.range(0, nY).parallel().forEach(i -> {
                IntStream.range(0, nX).parallel().forEach(j -> {
                    Color pixelColor;
                    Ray ray;
                    if(isAdaptiveSuperSamplingOn){
                        pixelColor = adaptiveAntiAliasing(nX,nY,j,i);
                    }
                    else if(isAntiAliasingOn)
                        pixelColor = antiAliasing(nX,nY,j,i);
                    else{
                        ray = constructRay(nX, nY, j, i);
                        pixelColor = rayTracer.traceRay(ray);
                    }
                    Pixel.pixelDone();
                    Pixel.printPixel();
                    imageWriter.writePixel(j, i, pixelColor);
                });
            });
        }
        else{
            Color pixelColor;
            Ray ray;
            for (int i = 0; i < nX; i++){
                for (int j = 0; j < nY; j++){
                    System.out.println(i + "," + j);
                    if(isAdaptiveSuperSamplingOn){
                        pixelColor = adaptiveAntiAliasing(nX,nY,j,i);
                    }
                    else if(isAntiAliasingOn)
                        pixelColor = antiAliasing(nX,nY,j,i);
                    else{
                        ray = constructRay(nX, nY, j, i);
                        pixelColor = rayTracer.traceRay(ray);
                    }
                    imageWriter.writePixel(j, i, pixelColor);
                }
            }
        }


        return this;
    }*/
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
        return adaptiveAntiAliasing_(center,pixelSize, adaptiveMaxDepth);
    }

    /**
     * This is the recursive function for adaptive super sampling
     * @param center
     * @param squareSize
     * @param depth
     * @return
     */
    private Color adaptiveAntiAliasing_(Point center, double squareSize, int depth){
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
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(0.25*squareSize)).add(vUp.scale(0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = center.add(vRight.scale(-0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            newCenter = p4.add(vRight.scale(0.25*squareSize)).add(vUp.scale(-0.25*squareSize));
            result = result.add(adaptiveAntiAliasing_(newCenter,0.5*squareSize,depth - 1));

            return result.scale(0.25);
        }
    }
    /* * Helper method for rendering image
     * renders a given pixel
     *
     * @param nX number of columns
     * @param nY number of rows
     * @param j  column of the pixel
     * @param i  row of the pixel
     */
   /* private void renderHelper(int nX, int nY, int j, int i) {
        //if anti aliasing is not set, construct one ray per pixel
        if (!isAntiAliasing) {
            // construct a ray through the current pixel
            Ray ray = this.constructRayThroughCenter(nX, nY, j, i);
            // get the  color of the point from trace ray
            Color color = rayTracer.traceRay(ray);
            // write the pixel color to the image
            imageWriter.writePixel(j, i, color);
        } else if (!isAdaptive) {
            List<Ray> rays = this.constructAntiAliasingRays(nX, nY, j, i);
            // get the  color of the point from trace ray
            Color color = rayTracer.traceRays(rays);
            // construct a ray through the current pixel
            // write the pixel color to the image
            imageWriter.writePixel(j, i, color);
        }

        //otherwise, do antialiasing
        else {
            // construct a ray through the current pixel
            List<Ray> rays = this.constructAntiAliasingRays(nX, nY, j, i);
            // get the  color of the point from trace ray
            Color color = rayTracer.adaptiveTraceRays(rays);
            // write the pixel color to the image
            imageWriter.writePixel(j, i, color);
        }
    }*/
    private Color castRay(int nX, int nY, double j, double i)
    {
        if(isAdaptiveSuperSamplingOn)
            return  adaptiveAntiAliasing(nX,nY,j,i);
        else if (isAntiAliasingOn)
        {
            List<Ray> rays = constructRays(nX,nY,j,i);
            Color color =Color.BLACK;
            for (Ray ray:rays)
            {
                Color color1 = rayTracer.traceRay(ray);
                color=color.add(color1);
            }
            return color.reduce(numOfRays);
        }
        else
        {
            Ray ray = constructRay(nX, nY, j, i);
            return rayTracer.traceRay(ray);
        }
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
        Point pCenterPixel = CalculatCenterPointInPixel(nX, nY, j, i);
        //adding the original ray to the rays list
        rays.add(constructRay(nX,nY,j,i));
        for (int k = 0; k < numOfRays; k++) {

            // Generating a random number between -rX/2 and rX/2.
            randX = random(-rX / 2, rX / 2);

            // Generating a random number between -rY/2 and rY/2.
            randY = random(-rY / 2, rY / 2);

            // Creating a new point that is a random distance from the center point.
            pInPixel = new Point(pCenterPixel.getX() + randX, pCenterPixel.getY() + randY, pCenterPixel.getZ());

            // Creating a ray from the camera to the pixel.
            rays.add(new Ray(p0, pInPixel.subtract(p0).normalize()));
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
    /* Setter of builder patters
     * sets the multithreading
     * If set to 1, the render won't use the thread pool.
            * If set to 0, the thread pool will pick the number of threads.
     *
             * @param threads number of threads to use
     * @return the current render
     * @throws IllegalArgumentException when threads is less than 0
            */
    public Camera setMultithreading(int threads) {
        if (threads < 0)//threads cannot be less than zero
            throw new IllegalArgumentException("Multithreading parameter must be 0 or higher");

        isMultithreading = true;

        if (threads != 0)//any number that is not zero is acceptable
            numOfThreads = threads;

        else {//if number received was zero:
            int cores = Runtime.getRuntime().availableProcessors() - 2; //leave 2 spare threads
            numOfThreads = cores <= 2 ? 1 : cores;//if cores is smaller than 2 than threads is 1, i=otherwise it is equal to cores
        }
        return this;
    }
}

