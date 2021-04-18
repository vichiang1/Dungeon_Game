package byow.Util;

import byow.Core.Floor;
import byow.Core.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationGraph implements AStarGraph<Location>{
    private Map<Location,List<WeightedEdge<Location>>> edges;
    private Floor f;


    public LocationGraph(Floor f, List<List<Location>> locations){
        this.f = f;
        edges = new HashMap();
        for(List<Location> locs: locations){
            Location source = locs.remove(0);
            ArrayList<WeightedEdge<Location>> outgoing = new ArrayList();
            for(Location l: locs){
                outgoing.add(new WeightedEdge<Location>(source,l,1));
            }
            this.edges.put(source,outgoing);
        }
    }

    public List<WeightedEdge<Location>> neighbors(Location v){
        return edges.get(v);
    }
    public double estimatedDistanceToGoal(Location s, Location goal){
        return f.estimateDistance(s,goal);
    }

    public boolean contains(Location loc){
        return edges.containsKey(loc);
    }

}
