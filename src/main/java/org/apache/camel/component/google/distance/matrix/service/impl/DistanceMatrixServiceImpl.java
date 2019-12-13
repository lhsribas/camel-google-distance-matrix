package org.apache.camel.component.google.distance.matrix.service.impl;

import org.apache.camel.component.google.distance.matrix.dto.FilterMatrix;
import org.apache.camel.component.google.distance.matrix.service.DistanceMatrixService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DistanceMatrixServiceImpl implements DistanceMatrixService {

    @Override
    public String distanceInfo(final FilterMatrix filterMatrix)  throws Exception {

        StringBuffer bufferUrl = new StringBuffer("https://maps.googleapis.com/maps/api/distancematrix/");
        bufferUrl.append(filterMatrix.getType());

        bufferUrl.append("?origins=").append(filterMatrix.getOrigins());
        bufferUrl.append("&destinations=").append(filterMatrix.getDestinations());
        bufferUrl.append("&mode=").append(filterMatrix.getMode());
        bufferUrl.append("&language=").append(filterMatrix.getLanguage());
        bufferUrl.append("&sensor=").append(filterMatrix.isSensor());
        bufferUrl.append("&units=").append(filterMatrix.getUnits());
        bufferUrl.append("&key=").append(filterMatrix.getKey());

        HttpURLConnection urlConnection = null ;

        try {
            URL url = new URL(bufferUrl.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            urlConnection.setConnectTimeout(filterMatrix.getConnectionTimeout());
            urlConnection.setReadTimeout(filterMatrix.getSocketTimeout());

            InputStream iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            iStream.close();

            return  sb.toString();

        } catch (MalformedURLException m) {
           throw new Exception(m);
        } catch (IOException i) {
           throw new Exception(i);
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
    }
}
