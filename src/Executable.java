import processing.core.PImage;

import java.util.List;

public abstract class Executable extends animatingEntity {
        private int actionPeriod;

        public Executable(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod){
                super(id, position, images, imageIndex, animationPeriod);
                this.actionPeriod = actionPeriod;
        }

        public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler){
                scheduler.scheduleEvent(this,
                        this.createActivityAction(world, imageStore),
                        this.getActionPeriod());
        }

        public int getActionPeriod(){return this.actionPeriod;}

}
