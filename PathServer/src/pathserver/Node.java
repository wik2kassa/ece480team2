package pathserver;

import java.util.List;
import java.util.ArrayList;

public class Node {
    private int id;
    private List<Node> neighbors;
    public double X,Y,Z;
    private double F, G, H;

    Node(){
        id = 0;
        F = 0;
        G = 0;
        H = 0;
        X = 0;
        Y = 0;
        Z = 0;
        neighbors = new ArrayList<Node>();
    }
    Node(double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
        F = 0;
        G = 0;
        H = 0;
        neighbors = new ArrayList<Node>();
        id = 0;
    }

    public List<Node> getNeighbor(){
        return neighbors;
    }
    public Node getNeighbor(int i){
        return neighbors.get(i);
    }
    public void addNeighbor(Node x){
        neighbors.add(x);
    }

    public int getId(){
        return id;
    }

    public void setCoordinates(double x, double y, double z){
        X = x;
        Y = y;
        Z = z;
    }

    public double getScore(){
        return F;
    }

    public void setScore(double g, double h){
        G = g;
        H = h;
        F = G + H;
    }
    public void setScore(double g){
        G = g;
        F = G + H;
    }
}

