import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public final class Background {
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public int getImageIndex() {
        return imageIndex;
    }

    public Background(String id, List<PImage> images) {
        this.id = id;
        this.images = images;
    }
    public static PImage getCurrentImage(Object entity) {
        return ((Background)entity).images.get(
                ((Background)entity).imageIndex);
    }
}
