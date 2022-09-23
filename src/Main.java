package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import src.Graph.Edge;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Please provide file path!");
            return;
        }
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        ArrayList<int[]> edges = new ArrayList<>();
        String line;
        int counter = 0;
        HashMap<String, Integer> m = new HashMap<>();
        HashMap<Integer, String> k = new HashMap<>();
        while((line = br.readLine()) != null)  {
            String[] split = line.split(" ");
            String a = split[0];
            String b = split[1];
            if(!m.containsKey(a)) {
                m.put(a, counter);
                counter += 1;
            }
            if(!m.containsKey(b)) {
                m.put(b, counter);
                counter += 1;
            }
            k.put(m.get(a), a);
            k.put(m.get(b), b);
            edges.add(new int[]{m.get(a), m.get(b)});

        } 
        br.close();
        System.out.println("Starting Serial");
        long startTime = System.currentTimeMillis();
        EdgeBetweeness eb = new EdgeBetweeness(edges, counter);
        ArrayList<Edge> flowEdges = eb.calculateSerial();
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel Time took: " +  (endTime-startTime));

        System.out.println("Starting Parallel");
        startTime = System.currentTimeMillis();
        eb = new EdgeBetweeness(edges, counter);
        flowEdges = eb.calculateParallel();
        endTime = System.currentTimeMillis();
        System.out.println("Parallel Time took: " +  (endTime-startTime));


    }
}
