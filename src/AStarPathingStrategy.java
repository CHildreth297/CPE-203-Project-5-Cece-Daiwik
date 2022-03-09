import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy {


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        List<Point> path = new LinkedList<Point>();
        Comparator<PathNode> fVals = Comparator.comparing(PathNode::getTotalDist);
        PriorityQueue<PathNode> openList = new PriorityQueue<>(fVals);
        // create hashmap for openlist - hashmap is more flexible than hashset because you have a key and can change the values at that key.
        // Hashset isn't as forgiving with trying to change the values there --> does this mean i would need to change the hashcode for hashmap?
        HashMap<Point, Double> openListMap = new HashMap();
        openList.add(new PathNode(start.x, start.y, null));
        // poll is popping values from priority queue
        PathNode curr = openList.poll();
        curr.setDfs(0);
        curr.setHeuristicDist(PathNode.heuristicDistance(curr.getPoint(), end));
        curr.setTotalDist(curr.totalDistance());
        openListMap.put(curr.getPoint(), curr.getDfs());
        HashSet<Point> closedList = new HashSet<>();


        // how to check within reach
        while (curr != null && !withinReach.test(curr.getPoint(), end)) {
            List<Point> validNeighbors = potentialNeighbors
                    .apply(curr.getPoint())
                    .filter(canPassThrough)
                    .filter(c -> !closedList.contains(c)).toList();
            //for each valid node
            for (Point n : validNeighbors) {
                // new node is neighbor of the current node
                PathNode newNode = new PathNode(n.x, n.y, curr);
                // determine the distance from start node (g)
                newNode.setDfs(PathNode.distanceFromStart(curr.getPoint(), start, newNode.getPoint()));

                // add node to open list if there is a duplicate
                if(openListMap.containsKey(newNode.getPoint())){
                    Point key = newNode.getPoint();
                    // replace the old value
                    if(openListMap.get(key) > newNode.getDfs()){
                        openListMap.put(key, newNode.getDfs());
                        openList.remove(newNode);
                    }
                    else{
                        continue;
                    }
                }

                newNode.setHeuristicDist(PathNode.heuristicDistance(newNode.getPoint(), end));
                newNode.setTotalDist(newNode.totalDistance());
                openList.add(newNode);
                openListMap.put(newNode.getPoint(), newNode.getDfs());

            }
            // move current node to closed list
            closedList.add(curr.getPoint());
            // choose node from open list with smallest f value and make it current node
            curr = openList.poll();
        }
        // build the path
            buildPath(curr, path);


        return path;
    }

    // helper function to build the path from the ending points to the starting point
    public void buildPath(PathNode curr, List<Point> path){
        if(curr == null || curr.getPriorNode() == null){
           return;
        }
        buildPath(curr.getPriorNode(), path);
        path.add(curr.getPoint());

    }


}


