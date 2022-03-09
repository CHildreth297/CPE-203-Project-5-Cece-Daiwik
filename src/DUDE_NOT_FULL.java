import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DUDE_NOT_FULL extends Dude
{



    public DUDE_NOT_FULL(
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod)
    {
        super(id, position, images, 0, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.getResourceCount() >= this.getResourceLimit()) {
            Entity miner = DUDE_FULL.create(this.getId(),
                    this.getPosition(), this.getActionPeriod(),
                    this.getAnimationPeriod(),
                    this.getResourceLimit(),
                    this.getImagesList());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            ((animatingEntity)miner).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }


    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> target =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(TREE.class, SAPLING.class)));

        if (!target.isPresent() || !this.moveToNotFull(world, (Health) target.get(), scheduler)
                || !this.transformNotFull(world, scheduler, imageStore))
        {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean moveToNotFull(
            WorldModel world,
            Health target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            this.setResourceCount(this.getResourceCount() + 1);
            target.setHealth(target.getHealth() - 1);
            return true;

        }
        else {
            Point nextPos = this.nextPositionDude(world, target.getPosition());

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
            int resourceLimit,
            List<PImage> images)
    {
        return new DUDE_NOT_FULL(id, position, images, resourceLimit, 0,
                actionPeriod, animationPeriod);
    }

}
