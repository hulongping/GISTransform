package com.gis.transform;

/**
 * Created by hulongping on 2017/4/9.
 */
public final class WebMercator
{
    public static final double DEGREES_PER_RADIANS = 180.0 / Math.PI;
    public static final double RADIANS_PER_DEGREES = Math.PI / 180.0;
    public static final double PI_OVER_2 = Math.PI / 2.0;
    public static final double RADIUS = 6378137.0;
    public static final double RADIUS_2 = RADIUS * 0.5;
    public static final double RAD_RAD = RADIANS_PER_DEGREES * RADIUS;


    public static void main(String[] args){
        double xys[] = lonlatToxy(121,31);
        double[] lonlats = xyToLonLat(xys[0],xys[1]);
        System.out.println(lonlats[0]+","+lonlats[1]);
    }





    public static double[] xyToLonLat(double x,double y){
        double lon=xToLongitude(x);
        double lat=yToLatitude(y);

        return new double[]{lon,lat};
    }


    public static double[] lonlatToxy(double lon,double lat){
        double x=longitudeToX(lon);
        double y=latitudeToY(lat);

        return new double[]{x,y};
    }





    /**
     * Convert geo lat to vertical distance in meters.
     *
     * @param latitude the latitude in decimal degrees.
     * @return the vertical distance in meters.
     */
    private static double latitudeToY(double latitude)
    {
        final double rad = latitude * RADIANS_PER_DEGREES;
        final double sin = Math.sin(rad);
        return RADIUS_2 * Math.log((1.0 + sin) / (1.0 - sin));
    }

    /**
     * Convert geo lon to horizontal distance in meters.
     *
     * @param longitude the longitude in decimal degrees.
     * @return the horizontal distance in meters.
     */
    private static double longitudeToX(double longitude)
    {
        return longitude * RAD_RAD;
    }

    /**
     * Convert horizontal distance in meters to longitude in decimal degress.
     *
     * @param x the horizontal distance in meters.
     * @return the longitude in decimal degrees.
     */
    private static double xToLongitude(final double x)
    {
        return xToLongitude(x, true);
    }

    /**
     * Convert horizontal distance in meters to longitude in decimal degress.
     *
     * @param x      the horizontal distance in meters.
     * @param linear if using continuous pan.
     * @return the longitude in decimal degrees.
     */
    private static double xToLongitude(
            final double x,
            final boolean linear)
    {
        final double rad = x / RADIUS;
        final double deg = rad * DEGREES_PER_RADIANS;
        if (linear)
        {
            return deg;
        }
        final double rotations = Math.floor((deg + 180.0) / 360.0);
        return deg - (rotations * 360.0);
    }

    /**
     * Convert vertical distance in meters to latitude in decimal degress.
     *
     * @param y the vertical distance in meters.
     * @return the latitude in decimal degrees.
     */
    private static double yToLatitude(final double y)
    {
        final double rad = PI_OVER_2 - (2.0 * Math.atan(Math.exp(-1.0 * y / RADIUS)));
        return rad * DEGREES_PER_RADIANS;
    }
}
