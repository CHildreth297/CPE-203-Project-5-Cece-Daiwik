import processing.core.PImage;

import java.util.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private int numRows;
    private int numCols;

    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;

    public int getNumRows(){return this.numRows;}

    public int getNumCols(){return this.numCols;}

    public Set<Entity> getEntities(){return this.entities;}

    public Background[][] getBackground() {
        return background;
    }

    public Entity[][] getOccupancy() {
        return occupancy;
    }

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.getY() >= 0 && pos.getY() < this.numRows && pos.getX() >= 0
                && pos.getX() < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.getPosition())) {
            this.setOccupancyCell(entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(
            Point pos) {
        if (this.withinBounds(pos)) {
            return Optional.of(Background.getCurrentImage(this.getBackgroundCell(pos)));
        } else {
            return Optional.empty();
        }
    }

    public void setBackground(
            Point pos, Background background) {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }
    private void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.getY()][pos.getX()] = background;
    }
    private Background getBackgroundCell(Point pos) {
        return this.background[pos.getY()][pos.getX()];
    }

    public void load(
            Scanner in, ImageStore imageStore) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!Functions.processLine(in.nextLine(), this, imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            } catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            } catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }



    public void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }
        this.addEntity(entity);
    }
    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.getPosition());
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.getY()][pos.getX()];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        this.occupancy[pos.getY()][pos.getX()] = entity;
    }

    private static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = Functions.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = Functions.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public Optional<Entity> findNearest(
            Point pos, List<Class> classes)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Class c: classes)
        {
            for (Entity entity : this.entities) {
                if (entity.getClass() == c) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }
}
