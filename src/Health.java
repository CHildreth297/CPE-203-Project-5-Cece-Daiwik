import processing.core.PImage;

import java.util.List;

public abstract class Health extends Executable{

        public int health = 0;
        public int healthLimit = 0;

        public Health(String id, Point position, List<PImage> images, int imageIndex, int actionPeriod, int animationPeriod, int health, int healthLimit){
            super(id, position, images, imageIndex, animationPeriod, actionPeriod);
            this.health = health;
            this.healthLimit = healthLimit;
        }

        public int getHealth() { return this.health;}
        public int getHealthLimit() {return this.healthLimit;}
        public int setHealth(int health){return this.health = health;}

}
