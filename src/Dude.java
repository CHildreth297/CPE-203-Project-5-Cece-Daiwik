import processing.core.PImage;

import java.util.List;
import java.util.Optional;

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

    public void transform( WorldModel world,
                           EventScheduler scheduler,
                           ImageStore imageStore, Entity miner){
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        ((animatingEntity)miner).scheduleActions(scheduler, world, imageStore);
    }

    public void move(WorldModel world,
                     Entity target,
                     EventScheduler scheduler){
        Point nextPos = this.nextPositionDude(world, target.getPosition());

        if (!this.getPosition().equals(nextPos)) {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent()) {
                scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
        }
    }


}
