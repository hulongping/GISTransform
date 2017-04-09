package com.gis.transform;

/**
 * Created by hulongping on 2017/4/9.
 */
public class CoordinateUtil {





    /**
     * 百度坐标系 (BD-09) 转 火星坐标系 (GCJ-02)
     *
     * @param bd_lon
     * @param bd_lat
     * @return
     */
    public static double[] bd09Togcj02(double bd_lon, double bd_lat) {

        double x = bd_lon - 0.0065;
        double y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new double[]{gg_lng, gg_lat};
    }


    /**
     * 火星坐标系 (GCJ-02)转 百度坐标系 (BD-09)
     *
     * @param lng
     * @param lat
     * @return
     */
    public static double[] gcj02Tobd09(double lng, double lat) {

        double z = Math.sqrt(lng * lng + lat * lat) + 0.00002 * Math.sin(lat * x_PI);
        double theta = Math.atan2(lat, lng) + 0.000003 * Math.cos(lng * x_PI);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new double[]{bd_lng, bd_lat};
    }

    ;


    /**
     * WGS84转GCj02
     *
     * @param lng WGS84坐标系的经度
     * @param lat WGS84坐标系的纬度
     * @return
     */
    public static double[] wgs84Togcj02(double lng, double lat) {
        if (out_of_china(lng, lat)) {
            return new double[]{lng, lat};
        } else {
            double dlat = transformlat(lng - 105.0, lat - 35.0);
            double dlng = transformlng(lng - 105.0, lat - 35.0);
            double radlat = lat / 180.0 * Math.PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = lat + dlat;
            double mglng = lng + dlng;
            return new double[]{mglng, mglat};
        }
    }


    /**
     * GCJ02(火星坐标系)转GPS84
     *
     * @param lng 火星坐标系经度
     * @param lat 火星坐标系纬度
     * @return
     */
    public static double[] gcj02towgs84(double lng, double lat) {
        if (out_of_china(lng, lat)) {
            return new double[]{lng, lat};
        } else {
            double dlat = transformlat(lng - 105.0, lat - 35.0);
            double dlng = transformlng(lng - 105.0, lat - 35.0);
            double radlat = lat / 180.0 * PI;
            double magic = Math.sin(radlat);
            magic = 1 - ee * magic * magic;
            double sqrtmagic = Math.sqrt(magic);
            dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
            dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
            double mglat = lat + dlat;
            double mglng = lng + dlng;
            return new double[]{lng * 2 - mglng, lat * 2 - mglat};
        }

    }




    /**
     * 判断是否在国内，不在国内不做偏移
     *
     * @param lng
     * @param lat
     * @return
     */
    public static boolean out_of_china(double lng, double lat) {

        // 纬度3.86~53.55,经度73.66~135.05
        return !(lng > 73.66 && lng < 135.05 && lat > 3.86 && lat < 53.55);
    }

    private static double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    public static double x_PI = Math.PI * 3000.0 / 180.0;
    public static double PI = Math.PI;
    public static double ee = 0.00669342162296594323;
    public static double a = 6378245.0;


    public static void main(String[] args){
        double lon=121.4;
        double lat=31.2;




        //验证两次转换是否相等
        System.out.println("WGS1984 ==> GCJ02");
        double[] gcj02Coors = wgs84Togcj02(lon,lat);
        double[] wgs1984Coors = gcj02towgs84(gcj02Coors[0],gcj02Coors[1]);
        System.out.println(wgs1984Coors[0]+","+wgs1984Coors[1]);


        System.out.println("GCJ02 ==> BD09");
        double[] bd09Coors=gcj02Tobd09(gcj02Coors[0],gcj02Coors[1]);
        double[] gcj02CoorsFromBd=bd09Togcj02(bd09Coors[0],bd09Coors[1]);
        System.out.println(gcj02CoorsFromBd[0]+","+gcj02CoorsFromBd[1]);





    }

}
