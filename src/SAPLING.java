import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class SAPLING extends Health
{

    private static final int TREE_ANIMATION_MAX = 600;
    private static final int TREE_ANIMATION_MIN = 50;
    private static final int TREE_ACTION_MAX = 1400;
    private static final int TREE_ACTION_MIN = 1000;
    private static final int TREE_HEALTH_MAX = 3;
    private static final int TREE_HEALTH_MIN = 1;

    private static final String STUMP_KEY = "stump";

    private static final int SAPLING_HEALTH_LIMIT = 10;
    private static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time


    public SAPLING(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health, int healthLimit)
    {
        super(id, position, images, 0, actionPeriod, animationPeriod, health, healthLimit);
    }

    public boolean transformPlant(WorldModel world,
                                          EventScheduler scheduler,
                                          ImageStore imageStore)
    {
        return this.transformSapling(world, scheduler, imageStore);

    }

    public boolean transformSapling(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (this.health <= 0) {
            Entity stump = STUMP.create(this.getId(),
                    this.getPosition(),
                    imageStore.getImageList(STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);

            return true;
        }
        else if (this.health >= this.healthLimit)
        {
            Entity tree = TREE.create("tree_" + this.getId(),
                    this.getPosition(),
                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
                    imageStore.getImageList(Functions.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            ((animatingEntity)tree).scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
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


    public static Entity create(
            String id,
            Point position,
            List<PImage> images)
    {
        return new SAPLING(id, position, images,
                SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        this.health++;
        if (!this.transformPlant(world, scheduler, imageStore))
        {
            super.executeActivity(world, imageStore, scheduler);
        }
    }


}
