import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class FAIRY extends Executable
{

    public FAIRY(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, actionPeriod, animationPeriod);
    }


    public Point nextPositionFairy(
            WorldModel world, Point destPos)
    {
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz, this.getPosition().getY());
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//        return newPos;

        PathingStrategy path = new AStarPathingStrategy();
        // fairies will get stuck because it's only moving the point - once A star is implemented, they should be moving around
        List<Point> paths = path.computePath(this.getPosition(),
                                             destPos,
                                            (Point p1) -> world.withinBounds(p1) && !world.isOccupied(p1),
                                            (Point p1, Point p2) -> Functions.adjacent(p1, p2), path.CARDINAL_NEIGHBORS);
        return (paths.size() == 0 ? this.getPosition() : paths.get(0));
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(STUMP.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveToFairy(world, (STUMP) fairyTarget.get(), scheduler)) {
                Entity sapling = SAPLING.create("sapling_" + this.getId(), tgtPos,
                        imageStore.getImageList(Functions.SAPLING_KEY));

                world.addEntity(sapling);
                ((animatingEntity)sapling).scheduleActions(scheduler, world, imageStore);
            }
        }

        super.executeActivity(world, imageStore, scheduler);
    }


    public boolean moveToFairy(
            WorldModel world,
            STUMP target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = this.nextPositionFairy(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }


    public static Entity create(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new FAIRY(id, position, images,
                actionPeriod, animationPeriod);
    }

}
