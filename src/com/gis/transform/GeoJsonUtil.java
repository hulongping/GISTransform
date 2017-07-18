package com.gis.transform;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hulongping on 2017/7/17.
 */
public class GeoJsonUtil {


    public static void main(String[] args){
        try {
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
