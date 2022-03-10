/**
 * Animation class
 */
public final class Animation implements Action {

    private Entity entity;
    private int repeatCount;

    public Animation(Entity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

   public void executeAction(EventScheduler scheduler) {
       ((animatingEntity)(this.entity)).nextImage();

       if (this.repeatCount != 1) {
           scheduler.scheduleEvent(this.entity,
                   ((animatingEntity)this.entity).createAnimationAction(
                           Math.max(this.repeatCount - 1, 0)),
                   ((animatingEntity)this.entity).getAnimationPeriod());
       }
    }

}