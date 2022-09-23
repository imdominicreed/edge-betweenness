package src;
import java.util.ArrayList;

public class Graph {
    Node[] nodes;
    ArrayList<Edge> edges;
    Graph(ArrayList<int[]> edges, int n) {
        nodes = new Node[n];
        this.edges = new ArrayList<Edge>();
        for(int i = 0; i < n; i++) nodes[i] = new Node(i);
        for(int[] e : edges) {
            Node a = nodes[e[0]];
            Node b = nodes[e[1]];
            Edge edge = new Edge(a,b);
            this.edges.add(edge);
            a.add(edge);
            b.add(edge);
        }
    }

    public  class Edge {
        volatile double flow;
        Node a, b;
        Edge(Node a, Node b) {
            this. a = a;
            this.b = b;
        }

        public Node getDestination(Node src) {
            return src.id == a.id ? b : a;
        }

        @Override
        public String toString() {
            return a.toString() + " " + b.toString() + ": " + flow;
        }
         
    }

    public  class Node extends ArrayList<Edge> {
        int id;
        Node(int id) {
            this.id = id;
        }
        @Override
        public String toString() {
            return Integer.toString(id);
        }
    }
}
