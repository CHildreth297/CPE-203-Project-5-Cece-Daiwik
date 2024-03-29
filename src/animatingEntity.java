import processing.core.PImage;

import java.util.List;

public abstract class animatingEntity extends Entity{
        private int animationPeriod;


        public animatingEntity(String id, Point position, List<PImage> images, int imageIndex, int animationPeriod){
                super(id, position, images, imageIndex);
                this.animationPeriod = animationPeriod;
        }

        public void nextImage() {
                this.setImageIndex((this.getImageIndex() + 1) % this.getImagesList().size());
        }


        public Action createAnimationAction(int repeatCount) {
                return new Animation(this, repeatCount);
        }

        public Action createActivityAction(
                WorldModel world, ImageStore imageStore)
        {
                return new Activity(this, world, imageStore);
        }

        public int getAnimationPeriod(){return this.animationPeriod;}

        public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
                scheduler.scheduleEvent(this,
                        this.createAnimationAction( 0),
                        this.getAnimationPeriod());
        }

}
