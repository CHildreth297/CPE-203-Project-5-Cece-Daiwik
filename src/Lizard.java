import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.List;

public final class Lizard extends Health{

    private static final int LIZARD_HEALTH_LIMIT = 15;
    private static final int LIZARD_ACTION_ANIMATION_PERIOD = 3000;


    public Lizard(String id,
                  Point position,
                  List<PImage> images,
                  int actionPeriod,
                  int animationPeriod,
                  int health, int healthLimit){
        super(id, position, images, 0, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        if(this.health == this.healthLimit){
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
        }
        else{
            this.health++;
            int intialPosX = this.getPosition().x;
            int initialPosY = this.getPosition().y;
            Point newPos = new Point(intialPosX + (int) ((Math.random() * 2) - 1), initialPosY + (int) (Math.random() * 2) - 1);
            while(!world.withinBounds(newPos) && world.isOccupied(newPos)){
                newPos = new Point(intialPosX + (int) ((Math.random() * 2) - 1), initialPosY + (int) (Math.random() * 2) - 1);
            }
            world.moveEntity(this, newPos);
        }


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
        return new Lizard(id, position, images,
                LIZARD_ACTION_ANIMATION_PERIOD, LIZARD_ACTION_ANIMATION_PERIOD, 0, LIZARD_HEALTH_LIMIT);
    }




}


