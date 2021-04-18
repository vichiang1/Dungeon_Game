package byow.Util;

import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AStarSolver<Vertex> {
    private HashMap<Vertex, Double> distTo;
    private ExtrinsicMinPQ<Vertex> fringe;
    private HashMap<Vertex, Vertex> edgeFrom;
    private AStarGraph<Vertex> input;
    private Vertex goal;
    private SolverOutcome result;
    private List<Vertex> solution;
    private double solutionTime = 0.0;
    private int numOperations = 0;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        this.input = input;
        result = SolverOutcome.UNSOLVABLE;
        goal = end;
        distTo = new HashMap();
        fringe = new ArrayHeapMinPQ();
        edgeFrom = new HashMap();
        solution = new ArrayList();
        distTo.put(start, 0.0);
        fringe.add(start, input.estimatedDistanceToGoal(start, end));
        while (fringe.size() > 0) {
            if (sw.elapsedTime() > timeout) {
                result = SolverOutcome.TIMEOUT;
                break;
            }
            Vertex s = fringe.removeSmallest();
            numOperations += 1;
            if (s.equals(end)) {
                result = SolverOutcome.SOLVED;
                break;
            }
            for (WeightedEdge e : input.neighbors(s)) {
                if (!e.equals(end)) {
                    relax(e);
                }
            }
        }
        if (result.equals(SolverOutcome.SOLVED)) {
            Vertex active = end;
            while (!active.equals(start)) {
                solution.add(0, active);
                active = edgeFrom.get(active);
            }
            solution.add(0, start);
        }
        solutionTime = sw.elapsedTime();

    }

    private void relax(WeightedEdge<Vertex> e) {
        Vertex source = e.from();
        Vertex dest = e.to();
        Double weight = e.weight();
        if (distTo.get(source) + weight < distTo.getOrDefault(dest, Double.POSITIVE_INFINITY)) {
            distTo.put(dest, distTo.get(source) + weight);
            edgeFrom.put(dest, source);
            if (fringe.contains(dest)) {
                fringe.changePriority(dest, distTo.get(dest)
                        + input.estimatedDistanceToGoal(dest, goal));
            } else {
                fringe.add(dest, distTo.get(dest)
                        + input.estimatedDistanceToGoal(dest, goal));
            }
        }

    }

    public SolverOutcome outcome() {
        return result;
    }

    public List<Vertex> solution() {
        return solution;
    }

    public double solutionWeight() {
        return distTo.get(goal);
    }

    public int numStatesExplored() {
        return numOperations;
    }

    public double explorationTime() {
        return solutionTime;
    }
}
