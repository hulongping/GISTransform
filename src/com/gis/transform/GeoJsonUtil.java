package com.gis.transform;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;
import org.osgeo.proj4j.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulongping on 2017/7/17.
 */
public class GeoJsonUtil {


    public  static String gbcrc16(String vData) {
        int por, vLen, i, j;
        vLen = vData.length();
        por = 0xFFFF;
        for (i = 0; i <= vLen - 1; i++) {
            por = por >> 8;
            por = por ^ (int) vData.substring(i, i + 1).toCharArray()[0];
            for (j = 0; j < 8; j++) {
                if ((por & 1) != 0) {
                    por = (por >> 1) ^ 0xA001;
                } else {
                    por = (por >> 1);
                }
            }
        }
        return String.format("%04X", por);
    }


    public static void main(String[] args){
        try {



            CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
            CRSFactory crsFactory = new CRSFactory();
            CoordinateReferenceSystem scc=crsFactory.createFromParameters("Albers_C","+x_0=0 +y_0=0 +lat_0=0 +lon_0=105 +lat_1=25 +lat_2=47 +proj=aea +datum=WGS84 +no_defs");
            CoordinateReferenceSystem src = crsFactory.createFromName("epsg:4326");
            CoordinateReferenceSystem dest = crsFactory.createFromName("epsg:3857");
            CoordinateTransform transform = ctFactory.createTransform(src, scc);

            ProjCoordinate srcPt = new ProjCoordinate(121, 31);
            ProjCoordinate destPt = new ProjCoordinate();

            transform.transform(srcPt, destPt);
            System.out.println(srcPt + " ==> " + destPt);


            String code="6D40";
            int len=668;
            String vData="ST=22;CN=2011;PW=123456;MN=SXHB0PD0100017;CP=&&DataTime=20170719132100;a34001-Min=0.026,a34001-Avg=0.038,a34001-Max=0.050,a34001-Flag=N;a50001-Min=126.41,a50001-Avg=126.71,a50001-Max=126.88,a50001-Flag=N;Ea34001-Min=12.45,Ea34001-Avg=12.45,Ea34001-Max=12.45,Ea34001-Flag=N;E02-Min=1.00,E02-Avg=1.00,E02-Max=1.00,E02-Flag=N;a01007-Min=1.00,a01007-Avg=1.36,a01007-Max=1.92,a01007-Flag=N;a010a34001-Min=45.21,a010a34001-Avg=45.58,a010a34001-Max=46.28,a010a34001-Flag=N;a01002-Min=43.67,a01002-Avg=43.67,a01002-Max=43.68,a01002-Flag=N;a01006-Min=96843.6,a01006-Avg=96894.9,a01006-Max=96960.6,a01006-Flag=N;a01008-Min=323.9,a01008-Avg=323.9,a01008-Max=324.0,a01008-Flag=N&&";
            System.out.println(gbcrc16(vData));
            System.out.println(vData.length());
            File f = new File("D:\\WWW\\JavaProject\\GISTransform\\demodata\\shriver.json");

            List<Feature> newFeatures=new ArrayList<>();


            InputStream inputStream=new FileInputStream(f);

            FeatureCollection featureCollection =
                    new ObjectMapper().readValue(inputStream, FeatureCollection.class);
            List<Feature> features = featureCollection.getFeatures();
            for(int i=0;i<features.size();i++){
                Feature feature = features.get(i);
                GeoJsonObject object  = feature.getGeometry();
                if(object instanceof Polygon){
                    Polygon polygon =(Polygon)object;


                    List<List<LngLatAlt>> pathss = polygon.getCoordinates();

                    for(int j=0;j<pathss.size();j++){
                        List<LngLatAlt> ring = pathss.get(j);


                        List<LngLatAlt> newRing=new ArrayList<LngLatAlt>();

                        for(LngLatAlt lla :ring){
                            double lon= lla.getLongitude();
                            double lat =lla.getLatitude();

                            //验证两次转换是否相等
                            double[] coors = CoordinateUtil.wgs84Togcj02(lon,lat);
                            lla.setLongitude(coors[0]);
                            lla.setLatitude(coors[1]);

                            newRing.add(lla);
                        }
                        pathss.set(j,newRing);

                    }
                    polygon.setCoordinates(pathss);
                    feature.setGeometry(polygon);
                    newFeatures.add(feature);

                }
                featureCollection.setFeatures(newFeatures);



            }

            File outfile = new File( "D:\\WWW\\JavaProject\\GISTransform\\demodata\\shriver_py.json");

            FileOutputStream outputStream = new FileOutputStream(outfile);

            ObjectMapper om = new ObjectMapper();
            om.writeValue(outputStream,featureCollection);


        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
