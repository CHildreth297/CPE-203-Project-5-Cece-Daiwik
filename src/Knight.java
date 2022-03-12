import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Knight extends Dude
{
    public static boolean isKnight = false;
    public Knight(String id,
                  Point position,
                  List<PImage> images,
                  int resourceLimit,
                  int actionPeriod,
                  int animationPeriod)
    {

        super(id, position, images, 0, resourceLimit, 0, actionPeriod, animationPeriod);
        isKnight = true;
        //super(id, position, images, 0, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    //protected boolean _moveToDudeHelper(Entity target) {
        //return true;
    //}

//    protected Entity _transformHelper()
//    {
//        // return another knight
////        return Factory.createDudeNotFull(this.getId(),
////                this.getPosition(), this.getActionPeriod(),
////                this.getAnimationPeriod(),
////                this.getResourceLimit(),
////                this.getImages());
//
//        return new DUDE_FULL(getId(), getPosition(), getImagesList(), getResourceLimit(), 0,
//                getActionPeriod(), getAnimationPeriod());
//
////        return new Knight("knight", )
////        Entity newKnight = Factory.createKnight("knight",
////                pressed,
////                720,
////                100,
////                4,
////                imageStore.getImageList("knight"));
//    }



//    protected boolean _executeActionHelper(WorldModel world,
//                                           ImageStore imageStore,
//                                           EventScheduler scheduler)
//    {
//        Optional<Entity> target =
//                world.findNearest(getPosition(), new ArrayList<>(Arrays.asList(new HOUSE(null, null, null))));
//        // need to change to lizard
//
//        if (target.isPresent() && move(world,
//                target.get(), scheduler);)
//        {
//            transform(world, scheduler, imageStore);
//            return false;
//        }
//        return true;
//    }

    // don't need transform FULL for knight
//    public void transformFull(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        Entity dude = Knight.create(this.getId(),
//                this.getPosition(), this.getActionPeriod(),
//                this.getAnimationPeriod(),
//                this.getResourceLimit(),
//                this.getImagesList());
//
//        super.transform(world, scheduler, imageStore, dude);
//    }

    public void executeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(this.getPosition(), new ArrayList<>(Arrays.asList(Lizard.class)));
        // lizard
        // just call super.transform

        if (fullTarget.isPresent() && this.moveToFull(world,
                fullTarget.get(), scheduler))
        {
            world.removeEntity(fullTarget.get());
            Entity dude = Knight.create(this.getId(),
                this.getPosition(), this.getActionPeriod(),
                this.getAnimationPeriod(),
                this.getResourceLimit(),
                this.getImagesList());

        super.transform(world, scheduler, imageStore, dude);

        }
        else {
            super.executeActivity(world, imageStore, scheduler);
        }
    }

    public boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(this.getPosition(), target.getPosition())) {
            return true;
        }
        else {
            super.move(world, target, scheduler);
            return false;
        }
    }

    public static Entity create(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            int resourceLimit,
            List<PImage> images) {
        return new Knight(id, position, images, resourceLimit,
                actionPeriod, animationPeriod);
    }



}
