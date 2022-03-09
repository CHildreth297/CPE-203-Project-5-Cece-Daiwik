import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class HOUSE extends Entity
{



    public HOUSE(
            String id,
            Point position,
            List<PImage> images)
    {
        super(id, position, images, 0);
    }


    public static Entity create(
            String id, Point position, List<PImage> images)
    {
        return new HOUSE(id, position, images);
    }


}
