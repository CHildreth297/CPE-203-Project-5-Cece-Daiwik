import processing.core.PImage;

import java.util.List;

public abstract class Dude extends Executable {
    private int resourceLimit;
    private int resourceCount;

    public Dude(String id, Point position, List<PImage> images, int imageindex, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod){
        super(id, position, images, 0, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount){
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit(){
        return resourceLimit;
    }

    public Point nextPositionDude(WorldModel world, Point destPos)
    {
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).getClass() != STUMP.class) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getX() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) &&  world.getOccupancyCell(newPos).getClass() != STUMP.class) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
        PathingStrategy path = new AStarPathingStrategy();
        //PathingStrategy path = new AStarPathingStrategy();
        // dude can pass through stumps
        List<Point> paths = path.computePath(this.getPosition(),
                destPos,
                (Point p1) -> (world.withinBounds(p1)
                        && (world.getOccupancyCell(p1) != null ? world.getOccupancyCell(p1).getClass() == STUMP.class : true)
                        && !world.isOccupied(p1)),
                (Point p1, Point p2) -> Functions.adjacent(p1, p2), path.CARDINAL_NEIGHBORS);
        return (paths.size() == 0 ? this.getPosition() : paths.get(0));
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());

    }
}
