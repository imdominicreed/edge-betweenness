package src;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import src.Graph.Node;
import src.Graph.Edge;

public class EdgeBetweeness {
    Graph g;
    int n;
    EdgeBetweeness(ArrayList<int[]> edges, int n) {
        this.n = n;
        g = new Graph(edges, n);
    }

    public ArrayList<Edge> calculateSerial() {
        for(int i = 0; i < n; i++) {
            calculateFlow(i);
        }
        for(Edge e : g.edges) e.flow /= 2;
        return g.edges;

    }
    public ArrayList<Edge> calculateParallel() {
        ExecutorService pool = Executors.newFixedThreadPool(12);
        for(int i = 0; i < n; i++) {
            final int node = i; 
            pool.execute(new Runnable() {
                public void run() {
                calculateFlow(node);
                }
            });
        }
        try {
            while(pool.awaitTermination(1, TimeUnit.NANOSECONDS));
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        pool.shutdownNow();
        for(Edge e : g.edges) e.flow /= 2;
        return g.edges;
    }

    private void buildLevelGraph(ArrayList<ArrayList<Node>> levels, ArrayList<ArrayList<Edge>> parentEdges, int[] paths) {
        boolean[] visited = new boolean[n];
        visited[levels.get(0).get(0).id] = true;
        int remainingNodes = n - 1;
        int currentLevel = 0;

        while(remainingNodes != 0) {
            ArrayList<Node> nextLevel = new ArrayList<>();
            boolean[] justAdded = new boolean[n];
            for(Node node : levels.get(currentLevel)) {
                for(Edge  edge : node) {
                    Node child = edge.getDestination(node);

                    if(visited[child.id] && !justAdded[child.id]) continue;
                    paths[child.id] += paths[node.id];
                    parentEdges.get(child.id).add(edge);

                    if(justAdded[child.id]) continue;
                    visited[child.id] = true;
                    justAdded[child.id] = true;
                    remainingNodes--;
                    nextLevel.add(child);
                }
            }
            
            levels.add(nextLevel);
            currentLevel++;
        }
    }

    private void addFlows(ArrayList<ArrayList<Node>> levels, ArrayList<ArrayList<Edge>> parentEdges, int[] paths) {
        double[] flows = new double[n];

        for(int i = levels.size() - 1; i > 0; i--) {
            for(Node node : levels.get(i)) {
                double flow = 1 + flows[node.id];
                int sumPath = paths[node.id];
                for(Edge parentEdge : parentEdges.get(node.id)) {
                    int parentId = parentEdge.getDestination(node).id;
                    int path = paths[parentId];
                    double flowCalculation =  flow * ((double)path/sumPath);
                    parentEdge.flow += flowCalculation;
                    flows[parentId] +=  flowCalculation;
                }
            }
        }

    }

    private void calculateFlow(int root) {
        //Initialize the first level with the root
        ArrayList<ArrayList<Node>> levels = new ArrayList<>();
        levels.add(new ArrayList<>());
        levels.get(0).add(g.nodes[root]);

        //Prepares parent adj matrix
        ArrayList<ArrayList<Edge>> parentEdges = new ArrayList<>();
        for(int i = 0; i< n; i++) parentEdges.add(new ArrayList<>());

        //Number of paths to node from root
        int[] paths = new int[n];
        paths[root] = 1; //root node starts at 1

        buildLevelGraph(levels, parentEdges, paths);

        addFlows(levels, parentEdges, paths);

    }

}