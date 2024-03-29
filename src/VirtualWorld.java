import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Optional;

import processing.core.*;

public final class VirtualWorld extends PApplet
{
    private static final int TIMER_ACTION_PERIOD = 100;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;
    private static final int WORLD_WIDTH_SCALE = 2;
    private static final int WORLD_HEIGHT_SCALE = 2;

    private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
    private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
    private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
    private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static String LOAD_FILE_NAME = "world.sav";

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private static double timeScale = 1.0;

    public static boolean worldTransformed;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    private long nextTime;

    public void settings() {
        size(VIEW_WIDTH, VIEW_HEIGHT);
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.imageStore = new ImageStore(
                createImageColored(TILE_WIDTH, TILE_HEIGHT,
                                   DEFAULT_IMAGE_COLOR));
        this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
                                    createDefaultBackground(imageStore));
        this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world, TILE_WIDTH,
                                  TILE_HEIGHT);
        this.scheduler = new EventScheduler(timeScale);

        loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
        loadWorld(world, LOAD_FILE_NAME, imageStore);

        scheduleActions(world, scheduler, imageStore);

        nextTime = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
    }

    public void draw() {
        long time = System.currentTimeMillis();
        if (time >= nextTime) {
            this.scheduler.updateOnTime(time);
            nextTime = time + TIMER_ACTION_PERIOD;
        }

        view.drawViewport();
    }

    private void transformWorld()
    {

        for(int i=0; i<world.getNumRows(); i++)
        {
            for(int j=0; j<world.getNumCols(); j++)
            {
                if(world.getBackground()[i][j].getId().equals("grass"))
                {
                    world.getBackground()[i][j] = new Background("lava1", imageStore.getImageList("lava1"));
                }
                else if(world.getBackground()[i][j].getId().equals("flowers") ||
                        world.getBackground()[i][j].getId().equals("mainBurn") ||
                        world.getBackground()[i][j].getId().equals("sideBurn3"))
                {
                    world.getBackground()[i][j] = new Background("lava2", imageStore.getImageList("lava2"));
                }
                /*
                else if(world.getBackground()[i][j].getId().equals("dirt") || 
                        world.getBackground()[i][j].getId().equals("dirt_horiz")
                world.getBackground()[i][j].getId().equals("dirt_vert_left")
                world.getBackground()[i][j].getId().equals("dirt_vert_right")
                world.getBackground()[i][j].getId().equals("dirt_bot_left_corner")
                world.getBackground()[i][j].getId().equals("dirt_bot_right_up")
                world.getBackground()[i][j].getId().equals("dirt_vert_left_bot"))
                {

                }

                 */
                else if(world.getBackground()[i][j].getId().equals("dirt_horiz"))
                {
                    world.getBackground()[i][j] = new Background("road3", imageStore.getImageList("road3"));
                }
                else if(world.getBackground()[i][j].getId().equals("dirt_vert_left") ||
                        world.getBackground()[i][j].getId().equals("dirt_vert_right") ||
                        world.getBackground()[i][j].getId().equals("dirt_bot_left_corner") ||
                        world.getBackground()[i][j].getId().equals("dirt_bot_right_up") ||
                        world.getBackground()[i][j].getId().equals("dirt_vert_left_bot"))
                {
                    world.getBackground()[i][j] = new Background("road2", imageStore.getImageList("road2"));
                }

                else if(world.getBackground()[i][j].getId().equals("bridge"))
                {
                    world.getBackground()[i][j] = new Background("lavaBridge", imageStore.getImageList("lavaBridge"));
                }
            }
        }

        imageStore.getMapOfImages().replace("lizard", imageStore.getImageList("dragon"));
        imageStore.getMapOfImages().replace("house", imageStore.getImageList("castle"));
        imageStore.getMapOfImages().replace("obstacle", imageStore.getImageList("lavaFlowing"));
        imageStore.getMapOfImages().replace("tree", imageStore.getImageList("egg"));


        for(Entity entity: world.getEntities().stream().toList())
        {
            entity.setImageList(imageStore.getImageList(entity.getId()));
            entity.setImageIndex(0);

            /*
            if(entity instanceof TREE)
            {


                Entity stump = new STUMP("stump", entity.getPosition(), imageStore.getImageList("egg"));

                world.removeEntityAt(entity.getPosition());
                scheduler.unscheduleAllEvents(entity);

                world.addEntity(stump);
            }
            */

        }
    }


    // Just for debugging and for P5
    public void mousePressed() {
        Point pressed = mouseToPoint(mouseX, mouseY);
        //System.out.println("CLICK! " + pressed.getX() + ", " + pressed.getY());
        //
        // imageStore.getMapOfImages().replace("lizard", imageStore.getImageList("dragon"));

        if(!checkSpotForDude(pressed) && !worldTransformed && !lizardClicked(pressed))
        {
            String id1 = "mainBurn";
            String id2 = "sideBurn3";
            world.setBackground(pressed, new Background(id1, imageStore.getImageList(id1)));

            for(Point point:getValidRippleNeighbors(pressed))
            {
                if(world.getOccupancyCell(point) != null)
                {
                    if(world.getOccupancyCell(point) instanceof Health)
                    {
                        world.removeEntityAt(point);
                    }
                    else
                    {
                        checkSpotForDude(point);
                    }
                }
                world.setBackground(point, new Background(id2, imageStore.getImageList(id2)));
            }
        }

        if(lizardClicked(pressed))
        {
            worldTransformed = true;
            transformWorld();
        }
    }

    private boolean lizardClicked(Point pressed)
    {
        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            return entity instanceof Lizard;
        }
        return false;
    }


    private boolean checkSpotForDude(Point pressed)
    {
        Optional<Entity> entityOptional = world.getOccupant(pressed);
        if (entityOptional.isPresent())
        {
            Entity entity = entityOptional.get();
            //System.out.println(entity.getId());

            if(entity instanceof Dude)
            {
                world.removeEntityAt(entity.getPosition());
                scheduler.unscheduleAllEvents(entity);

                Entity newKnight = new Knight("knight",
                        pressed, imageStore.getImageList("knight"), 4, 720, 100);

                world.addEntity(newKnight);

                ((animatingEntity)newKnight).scheduleActions(scheduler, world, imageStore);

                return true;
            }
        }
        return false;
    }


    private List<Point> getValidRippleNeighbors(Point point)
    {
        List<Point> validRippleNeighbors = new ArrayList<>();

        for(int i=-1; i<=1; i++)
        {
            for(int j=-1; j<=1; j++)
            {
                Point temp = new Point(point.x + i, point.y + j);
                if(world.withinBounds(temp) && !(i==0 && j==0))
                {
                    validRippleNeighbors.add(temp);
                }
            }
        }

        return validRippleNeighbors;
    }

    private Point mouseToPoint(int x, int y)
    {
        return view.getViewport().viewportToWorld(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
    }
    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP:
                    dy = -1;
                    break;
                case DOWN:
                    dy = 1;
                    break;
                case LEFT:
                    dx = -1;
                    break;
                case RIGHT:
                    dx = 1;
                    break;
            }
            view.shiftView(dx, dy);
        }
    }

    public static Background createDefaultBackground(ImageStore imageStore) {
        return new Background(DEFAULT_IMAGE_NAME,
                imageStore.getImageList(DEFAULT_IMAGE_NAME));
    }

    public static PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        for (int i = 0; i < img.pixels.length; i++) {
            img.pixels[i] = color;
        }
        img.updatePixels();
        return img;
    }

    static void loadImages(
            String filename, ImageStore imageStore, PApplet screen)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            imageStore.loadImages(in, screen);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void loadWorld(
            WorldModel world, String filename, ImageStore imageStore)
    {
        try {
            Scanner in = new Scanner(new File(filename));
            world.load(in, imageStore);
        }
        catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void scheduleActions(
            WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        for (Entity entity : world.getEntities()) {
            if(entity instanceof animatingEntity) {
                ((animatingEntity) entity).scheduleActions(scheduler, world, imageStore);
            }
        }
    }

    public static void parseCommandLine(String[] args) {
        if (args.length > 1)
        {
            if (args[0].equals("file"))
            {

            }
        }
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG:
                    timeScale = Math.min(FAST_SCALE, timeScale);
                    break;
                case FASTER_FLAG:
                    timeScale = Math.min(FASTER_SCALE, timeScale);
                    break;
                case FASTEST_FLAG:
                    timeScale = Math.min(FASTEST_SCALE, timeScale);
                    break;
            }
        }
    }

    public static void main(String[] args) {
        parseCommandLine(args);
        PApplet.main(VirtualWorld.class);
    }
}
