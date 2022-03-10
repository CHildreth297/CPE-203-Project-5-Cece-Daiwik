import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class OBSTACLE extends animatingEntity
{

    public OBSTACLE(
            String id,
            Point position,
            List<PImage> images,
            int animationPeriod)
    {
        super(id, position, images, 0, animationPeriod);
    }

    public static Entity create(
            String id, Point position, int animationPeriod, List<PImage> images)
    {
        return new OBSTACLE(id, position, images, animationPeriod);
    }

}
