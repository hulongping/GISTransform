package com.gis.transform;

/**
 * Created by hulongping on 2017/4/9.
 */
public class CoordinateUtil {


    /**
     * WebMercator转经纬度
     * @param mercatorX
     * @param mercatorY
     * @return
     */
    public static double[] mercator2lonLat(double mercatorX,double mercatorY){
        double x=mercatorX/20037508.34*180;
        double y=mercatorY/20037508.34*180;
        y= 180/Math.PI*(2*Math.atan(Math.exp(y*Math.PI/180))-Math.PI/2);
        double[] coors=new double[]{x,y};
        return coors;
    }


    /**
     * 经纬度转墨卡托
     * @param lon
     * @param lat
     * @return
     */
    public static double[] lonLat2mercator(double lon, double lat){
        double x = (lon/180) * 20037508.34;
        double tmp = 0;
        if(lat > 85.05112){ tmp = 85.05112;}
        if(lat < -85.05112){ tmp = -85.05112;}
        tmp = (Math.PI / 180.0) * tmp;
        double y = 20037508.34 * Math.log(Math.tan(Math.PI / 4.0 + tmp / 2.0)) / Math.PI;
        return new double[]{x,y};
    }


}
