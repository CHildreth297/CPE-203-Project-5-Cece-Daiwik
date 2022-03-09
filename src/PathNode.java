import java.util.Objects;



    public class PathNode {
        private int xVal;
        private int yVal;
        private Point point;
        //dfs = distance from start (g)
        private double dfs;
        // heuristic distance (h)
        private double heuristicDist;
        //total distance (f)
        private double totalDist;
        // priorNode = 'current' val
        private PathNode priorNode;

        public PathNode(int x, int y, PathNode priorNode){
            this.xVal = x;
            this.yVal = y;
            this.point = new Point(x, y);
            this.priorNode = priorNode;

        }


        public Point getPoint(){
            return point;
        }

        public double getDfs() {
            return dfs;
        }

        public double getHeuristicDist(){
            return heuristicDist;
        }

        public double getTotalDist(){
            return totalDist;
        }

        public void setDfs(double newDfs){
            this.dfs = newDfs;
        }

        public void setHeuristicDist(double heuristicDist) {
            this.heuristicDist = heuristicDist;
        }

        public void setTotalDist(double totalDist) {
            this.totalDist = totalDist;
        }

        public void setPriorNode(PathNode node){
            priorNode = node;
        }

        public PathNode getPriorNode() {
            return priorNode;
        }

        public static double distanceFromStart(Point curr, Point start, Point adj){
            // distance from current node to start + distance to current node yo adj
            return distance(curr, start) + distance(curr, adj);

        }

        public static double heuristicDistance(Point adj, Point end){
            // returns the distance from adjacent point to end point
            return distance(adj, end);
        }

        public double totalDistance(){
            return this.dfs + this.heuristicDist;
        }

        private static double distance(Point a, Point b){
            return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
        }

        // override hashset equals method for PathNode
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            PathNode p = (PathNode) o;
            return this.getPoint().equals(((PathNode) o).getPoint());
        }

        //override hashset hashcode method
        public int hashCode(){
            return Objects.hash(xVal, yVal, point, dfs, heuristicDist, totalDist, priorNode);
        }
    }


