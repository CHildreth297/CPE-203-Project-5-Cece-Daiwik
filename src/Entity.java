import processing.core.PImage;

import java.util.List;

public abstract class Entity {
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;


    public Entity(String id, Point position, List<PImage> images, int imageIndex){
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public int getImageIndex(){
        return this.imageIndex;
    }

    public PImage getCurrentImage(){return (this).images.get((this).imageIndex);}

    public List getImagesList(){return this.images;}

    public void setImageList(List<PImage> newImages)
    {
        this.images = newImages;
    }

    public Point getPosition(){return this.position;}

    public Point setPosition(Point pos){return this.position = pos;}

    public String getId(){return this.id;}







}
