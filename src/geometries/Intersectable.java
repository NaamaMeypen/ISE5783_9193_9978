package geometries;

import primitives.*;

import java.util.List;

public abstract class Intersectable {
    public List<Point> findIntersections(Ray ray)
    {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }    /**
     * inner class represents GeoPoint
     */
    public static class GeoPoint {
        /**
         * related geometry
         */
        public Geometry geometry;
        /**
         * point on geometry
         */
        public Point point;

        /**
         * GeoPoint constructor based-on geometry and point
         * @param geometry {@link Geometry}
         * @param point {@link Point}
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return this.geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }
        /** textual description of GeoPoint
         * @return textual description of GeoPoint
         */
        @Override
        public String toString() {
            return "GeoPoint{" +
                    "_geometry=" + geometry +
                    ", _point=" + point +
                    '}';
        }
    }
    public List<GeoPoint> findGeoIntersections(Ray ray){
        return findGeoIntersectionsHelper(ray);
    }
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);



}
