import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.List;

public final class Lizard extends Health{

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
            health++;
//            int initialPosX = this.getPosition().x;
//            int initialPosY = this.getPosition().y;
//            Point newPos = new Point(initialPosX + (int) ((Math.random() * 2) - 1), initialPosY + (int) (Math.random() * 2) - 1);
//            while(!world.withinBounds(newPos) && world.isOccupied(newPos)){
//                newPos = new Point(initialPosX + (int) ((Math.random() * 2) - 1), initialPosY + (int) (Math.random() * 2) - 1);
//            }
//            world.moveEntity(this, newPos);
            Point desPos = new Point((int)((Math.random() * 60) - 10), (int)((Math.random() * 60) - 10));
            while(!world.withinBounds(desPos) || world.isOccupied(desPos)){
                desPos = new Point((int)((Math.random() * 60) - 10), (int)((Math.random() * 60) - 10));
            }
            Point newPos = this.nextPositionLizard(world, desPos);
            world.moveEntity(this, newPos);
        }
        super.executeActivity(world, imageStore, scheduler);


    }

    public Point nextPositionLizard(WorldModel world, Point desPos){
        PathingStrategy path = new SingleStepPathingStrategy();
        // fairies will get stuck because it's only moving the point - once A star is implemented, they should be moving around
        List<Point> paths = path.computePath(this.getPosition(),
                desPos,
                (Point p1) -> world.withinBounds(p1) && !world.isOccupied(p1),
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


    public static Entity create(
            String id,
            Point position,
            List<PImage> images,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        return new Lizard(id, position, images,
                actionPeriod, animationPeriod, health, healthLimit);
    }




}


