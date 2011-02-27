package pathserver;

import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

public class Map {
    private List<Node> open;
    private List<Node> closed;

    Map(){
        open = new ArrayList<Node>();
        closed = new ArrayList<Node>();
    }

    public List<Node> getDirection(Node start, Node end){
        List<Node> path = new ArrayList<Node>();
        Node parent = start;
        double low = 1000;
        int index;

        closed.add(start);

        for(int i = 0; i < start.getNeighbor().size(); i++){
            open.add(start.getNeighbor(i));
            open.get(i).setScore(Distance(open.get(i),end), 0);
            if(low > open.get(i).getScore()){
                low = open.get(i).getScore();
                index = i;
            }
        }

        while(parent.getId() != end.getId()){

        }

        return path;
    }

    public double Distance(Node x, Node y){
        double tmp = (y.X - x.X) + (y.Y - x.Y) + (y.Z - x.Z);
        double dist = Math.sqrt(tmp);
        return dist;
    }

    public int Lowest(List<Node> l){
        int i = 0;
        return i;
    }
}
