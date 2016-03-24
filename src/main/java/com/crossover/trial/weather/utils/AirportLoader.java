package com.crossover.trial.weather.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.client.ClientResponse;

/**
 * A simple airport loader which reads a file from disk and sends entries to the
 * webservice
 *
 * TODO: Implement the Airport Loader
 * 
 * @author code test administrator
 */
public class AirportLoader {
    
    private static final String BASE_URI = "http://localhost:9090";

    /** end point for read queries */
    private WebTarget queryWebTarget;

    /** end point to supply updates */
    private WebTarget collectWebTarget;

    public AirportLoader() {
        Client client = ClientBuilder.newClient();
        queryWebTarget = client.target(BASE_URI).path("query");
        collectWebTarget = client.target(BASE_URI).path("collect");
    }
    
    private static final String SEPARATOR = ",";

    public void upload(BufferedReader reader) throws IOException {
        List<List<String>> list = reader.lines()
                .map(line -> Arrays.asList(line.split(SEPARATOR)))
                .collect(Collectors.toList());
        
        for (List<String> line : list) {
            String path = "/airport/" + line.get(4).replaceAll("\"", "") + "/" + line.get(6) + "/" + line.get(7);
           // System.out.println(path);
            collectWebTarget.path(path).request().post(Entity.entity("", MediaType.TEXT_HTML_TYPE));
        }
    }
   
    public static void main(String args[]) throws Exception {
        // Stream<String> stream = Files
        // .lines(Paths.get(args[0]));

        BufferedReader br = Files
                .newBufferedReader(Paths.get("D:\\Crossover\\airport-weather\\src\\main\\resources\\airports.dat"));
        AirportLoader al = new AirportLoader();
        al.upload(br);
        System.exit(0);
    }
}
