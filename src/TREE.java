import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class TREE extends Health
{

    private static final String STUMP_KEY = "stump";

    public TREE(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        super(id, position, images, 0, actionPeriod, animationPeriod, health, healthLimit);
    }

   public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {

        if (!this.transformPlant(world, scheduler, imageStore)) {

           super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean transformPlant(WorldModel world,
                                          EventScheduler scheduler,
                                          ImageStore imageStore)
    {
            return this.transformTree(world, scheduler, imageStore);

    }

    public boolean transformTree(
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

        return false;
    }

    public static Entity create(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int health,
            List<PImage> images)
    {
        return new TREE(id, position, images, actionPeriod, animationPeriod, health, 0);
    }

}
