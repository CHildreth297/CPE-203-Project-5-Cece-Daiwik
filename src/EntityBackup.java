//import processing.core.PImage;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
///**
// * An entity that exists in the world. See EntityKind for the
// * different kinds of entities that exist.
// */
//public final class EntityBackup
//{
//    private EntityKind kind;
//    private String id;
//    private Point position;
//    private List<PImage> images;
//
//    private int imageIndex;
//    private int resourceLimit;
//    private int resourceCount;
//    private int actionPeriod;
//    private int animationPeriod;
//    private int health;
//    private int healthLimit;
//
//    private static final int TREE_ANIMATION_MAX = 600;
//    private static final int TREE_ANIMATION_MIN = 50;
//    private static final int TREE_ACTION_MAX = 1400;
//    private static final int TREE_ACTION_MIN = 1000;
//    private static final int TREE_HEALTH_MAX = 3;
//    private static final int TREE_HEALTH_MIN = 1;
//
//    private static final String STUMP_KEY = "stump";
//
//
//    public EntityBackup(
//            EntityKind kind,
//            String id,
//            Point position,
//            List<PImage> images,
//            int resourceLimit,
//            int resourceCount,
//            int actionPeriod,
//            int animationPeriod,
//            int health,
//            int healthLimit)
//    {
//        this.kind = kind;
//        this.id = id;
//        this.position = position;
//        this.images = images;
//        this.imageIndex = 0;
//        this.resourceLimit = resourceLimit;
//        this.resourceCount = resourceCount;
//        this.actionPeriod = actionPeriod;
//        this.animationPeriod = animationPeriod;
//        this.health = health;
//        this.healthLimit = healthLimit;
//    }
//    public EntityKind getEntityKind(){ return this.kind; }
//
//    public String getId(){ return this.id; }
//
//    public Point getPosition(){ return this.position; }
//
//    public Point setPosition(Point position){return this.position = position;}
//
//    public int getImageIndex() {
//        return imageIndex;
//    }
//
//    public int getResourceLimit() {
//        return resourceLimit;
//    }
//
//    public int getResourceCount() {
//        return resourceCount;
//    }
//
//    public int getActionPeriod() {
//        return actionPeriod;
//    }
//
//    public int getHealth() {return health;}
//
//    public int getHealthLimit() {
//        return healthLimit;
//    }
//
//    public PImage getCurrentImage() {
//            return (this).images.get((this).imageIndex);
//
//    }
//
//    public Point nextPositionFairy(
//            WorldModel world, Point destPos)
//    {
//        int horiz = Integer.signum(destPos.getX() - this.position.getX());
//        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.getY() - this.position.getY());
//            newPos = new Point(this.position.getX(), this.position.getY() + vert);
//
//            if (vert == 0 || world. isOccupied(newPos)) {
//                newPos = this.position;
//            }
//        }
//        return newPos;
//    }
//
//    public Point nextPositionDude(WorldModel world, Point destPos)
//    {
//        int horiz = Integer.signum(destPos.getX() - this.position.getX());
//        Point newPos = new Point(this.position.getX() + horiz, this.position.getY());
//
//        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
//            int vert = Integer.signum(destPos.getY() - this.position.getY());
//            newPos = new Point(this.position.getX(), this.position.getX() + vert);
//
//            if (vert == 0 || world.isOccupied(newPos) &&  world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
//    }
//
//    public boolean transformNotFull(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        if (this.resourceCount >= this.resourceLimit) {
//            EntityBackup miner = Functions.createDudeFull(this.id,
//                    this.position, this.actionPeriod,
//                    this.animationPeriod,
//                    this.resourceLimit,
//                    this.images);
//
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(miner);
//            miner.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public void transformFull(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        EntityBackup miner = Functions.createDudeNotFull(this.id,
//                this.position, this.actionPeriod,
//                this.animationPeriod,
//                this.resourceLimit,
//                this.images);
//
//        world.removeEntity(this);
//        scheduler.unscheduleAllEvents(this);
//
//        world.addEntity(miner);
//        miner.scheduleActions(scheduler, world, imageStore);
//    }
//
//    public void executeSaplingActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        this.health++;
//        if (!this.transformPlant(world, scheduler, imageStore))
//        {
//            scheduler.scheduleEvent(this,
//                    this.createActivityAction(world, imageStore),
//                    this.actionPeriod);
//        }
//    }
//
//    public void executeTreeActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//
//        if (!this.transformPlant(world, scheduler, imageStore)) {
//
//            scheduler.scheduleEvent(this,
//                    this.createActivityAction(world, imageStore),
//                    this.actionPeriod);
//        }
//    }
//
//    public void executeFairyActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        Optional<EntityBackup> fairyTarget =
//                world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.STUMP)));
//
//        if (fairyTarget.isPresent()) {
//            Point tgtPos = fairyTarget.get().position;
//
//            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {
//                EntityBackup sapling = Functions.createSapling("sapling_" + this.id, tgtPos,
//                        imageStore.getImageList(Functions.SAPLING_KEY));
//
//                world.addEntity(sapling);
//                sapling.scheduleActions(scheduler, world, imageStore);
//            }
//        }
//
//        scheduler.scheduleEvent(this,
//                this.createActivityAction(world, imageStore),
//                this.actionPeriod);
//    }
//    public int getAnimationPeriod() {
//        switch (this.kind) {
//            case DUDE_FULL:
//            case DUDE_NOT_FULL:
//            case OBSTACLE:
//            case FAIRY:
//            case SAPLING:
//            case TREE:
//                return this.animationPeriod;
//            default:
//                throw new UnsupportedOperationException(
//                        String.format("getAnimationPeriod not supported for %s",
//                                this.kind));
//        }
//    }
//
//    public void nextImage() {
//        this.imageIndex = (this.imageIndex + 1) % this.images.size();
//    }
//
//    public void executeDudeNotFullActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        Optional<EntityBackup> target =
//                world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));
//
//        if (!target.isPresent() || !this.moveToNotFull(world,
//                target.get(),
//                scheduler)
//                || !this.transformNotFull(world, scheduler, imageStore))
//        {
//            scheduler.scheduleEvent(this,
//                    this.createActivityAction(world, imageStore),
//                    this.actionPeriod);
//        }
//    }
//
//    public void executeDudeFullActivity(
//            WorldModel world,
//            ImageStore imageStore,
//            EventScheduler scheduler)
//    {
//        Optional<EntityBackup> fullTarget =
//                world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.HOUSE)));
//
//        if (fullTarget.isPresent() && this.moveToFull(world,
//                fullTarget.get(), scheduler))
//        {
//            this.transformFull(world, scheduler, imageStore);
//        }
//        else {
//            scheduler.scheduleEvent(this,
//                    this.createActivityAction(world, imageStore),
//                    this.actionPeriod);
//        }
//    }
//
//    public boolean transformPlant(WorldModel world,
//                                          EventScheduler scheduler,
//                                          ImageStore imageStore)
//    {
//        if (this.kind == EntityKind.TREE)
//        {
//            return this.transformTree(world, scheduler, imageStore);
//        }
//        else if (this.kind == EntityKind.SAPLING)
//        {
//            return this.transformSapling(world, scheduler, imageStore);
//        }
//        else
//        {
//            throw new UnsupportedOperationException(
//                    String.format("transformPlant not supported for %s", this));
//        }
//    }
//
//    public boolean transformTree(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        if (this.health <= 0) {
//            Entity stump = STUMP.create(this.id,
//                    this.position,
//                    imageStore.getImageList(STUMP_KEY));
//
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(stump);
//            stump.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean transformSapling(
//            WorldModel world,
//            EventScheduler scheduler,
//            ImageStore imageStore)
//    {
//        if (this.health <= 0) {
//            Entity stump = Functions.createStump(this.id,
//                    this.position,
//                    imageStore.getImageList(STUMP_KEY));
//
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(stump);
//            stump.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//        }
//        else if (this.health >= this.healthLimit)
//        {
//            EntityBackup tree = Functions.createTree("tree_" + this.id,
//                    this.position,
//                    Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN),
//                    Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN),
//                    Functions.getNumFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN),
//                    imageStore.getImageList(Functions.TREE_KEY));
//
//            world.removeEntity(this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity(tree);
//            tree.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean moveToFairy(
//            WorldModel world,
//            EntityBackup target,
//            EventScheduler scheduler)
//    {
//        if (Functions.adjacent(this.position, target.position)) {
//            world.removeEntity(target);
//            scheduler.unscheduleAllEvents(target);
//            return true;
//        }
//        else {
//            Point nextPos = this.nextPositionFairy(world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                Optional<EntityBackup> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public boolean moveToNotFull(
//            WorldModel world,
//            EntityBackup target,
//            EventScheduler scheduler)
//    {
//        if (Functions.adjacent(this.position, target.position)) {
//            this.resourceCount += 1;
//            target.health--;
//            return true;
//        }
//        else {
//            Point nextPos = this.nextPositionDude(world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                Optional<EntityBackup> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public boolean moveToFull(
//            WorldModel world,
//            EntityBackup target,
//            EventScheduler scheduler)
//    {
//        if (Functions.adjacent(this.position, target.position)) {
//            return true;
//        }
//        else {
//            Point nextPos = this.nextPositionDude(world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                Optional<EntityBackup> occupant = world.getOccupant(nextPos);
//                if (occupant.isPresent()) {
//                    scheduler.unscheduleAllEvents(occupant.get());
//                }
//
//                world.moveEntity(this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public void scheduleActions(
//            EventScheduler scheduler,
//            WorldModel world,
//            ImageStore imageStore)
//    {
//        switch (this.kind) {
//            case DUDE_FULL:
//                scheduler.scheduleEvent(this,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction(0),
//                        this.getAnimationPeriod());
//                break;
//
//            case DUDE_NOT_FULL:
//                scheduler.scheduleEvent(this,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction( 0),
//                        this.getAnimationPeriod());
//                break;
//
//            case OBSTACLE:
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction( 0),
//                        this.getAnimationPeriod());
//                break;
//
//            case FAIRY:
//                scheduler.scheduleEvent(this,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction(0),
//                        this.getAnimationPeriod());
//                break;
//
//            case SAPLING:
//                scheduler.scheduleEvent(this,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction( 0),
//                        this.getAnimationPeriod());
//                break;
//
//            case TREE:
//                scheduler.scheduleEvent(this,
//                        this.createActivityAction(world, imageStore),
//                        this.actionPeriod);
//                scheduler.scheduleEvent(this,
//                        this.createAnimationAction( 0),
//                        this.getAnimationPeriod());
//                break;
//
//            default:
//        }
//    }
//    public Action createAnimationAction(int repeatCount) {
//        return new Animation(this, repeatCount);
//    }
//
//    private Action createActivityAction(
//            WorldModel world, ImageStore imageStore)
//    {
//        return new Activity(this, world, imageStore);
//    }
//
//}
