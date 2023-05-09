package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

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
    public Camera(Point _p0,Vector _vTo,Vector _vUp)
    {
        if(!isZero(_vTo.dotProduct(_vUp)))
        {
            throw new IllegalArgumentException("The vectors _vUp and _vTo are not perpendicular");
        }
        p0 = _p0;
        vTo=_vTo.normalize();
        vUp=_vUp.normalize();
        vRight=_vTo.crossProduct(_vUp).normalize();
    }
    public Camera setVPSize(double _width, double _height)
    {
       width=_width;
       height=_height;
       return this;
    }
    public Camera setVPDistance(double _distance)
    {
        distance=_distance;
        return this;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
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
       Point Pc=p0.add(vTo.scale(distance));
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

}
