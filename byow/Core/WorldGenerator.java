package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

import java.util.HashSet;
import java.util.ArrayList;

public class WorldGenerator {
    private int width;
    private int height;
    private Integer seed;
    private ArrayList<Room> rooms;
    private TETile[][] world;
    private HashSet<Point> points;
    private Random random;
    private double ratio = 0.4;
    private Point avatar = null;
    private DoubleMapPQ<Room> queue;
    private TETile theme;
    private TETile customavatar;
    boolean lineofsight = false;
    String direction = "None";
    private class Room {
        Point topright;
        Point bottomleft;
        Point center;
        Room(Point a, Point b) {
            topright = a;
            bottomleft = b;
            center = new Point((topright.getX() + bottomleft.getX()) / 2,
                    (topright.getY() + bottomleft.getY()) / 2);
        }
    }
    private boolean validRoom(Room room) {
        return room.topright.getY() - room.bottomleft.getY() > 0
                && room.topright.getX() - room.bottomleft.getX() > 0
                && room.topright.getY() > 0 && room.bottomleft.getY() > 2
                && room.topright.getX() > 0 && room.bottomleft.getX() > 0
                && !overlap(room);
    }
    private boolean overlap(Room room) {
        Point topleft = new Point(room.bottomleft.getX(), room.topright.getY());
        Point bottomright = new Point(room.topright.getX(), room.bottomleft.getY());
//        for (int curr = topleft.x; curr <= room.topright.x; curr = curr + 1) {
//            Point point = new Point(curr, topleft.y);
//            if (points.contains(point)) {
//                return true;
//            }
//        }
//        for (int curr = room.bottomleft.x; curr <= bottomright.x; curr = curr + 1) {
//            Point point = new Point(curr, bottomright.y);
//            if (points.contains(point)) {
//                return true;
//            }
//        }
//        for (int curr = room.bottomleft.y; curr <= topleft.y; curr = curr + 1) {
//            Point point = new Point(topleft.x, curr);
//            if (points.contains(point)) {
//                return true;
//            }
//        }
//        for (int curr = bottomright.y; curr <= room.topright.y; curr = curr + 1) {
//            Point point = new Point(bottomright.x, curr);
//            if (points.contains(point)) {
//                return true;
//            }
//        }
//        return false;
        for (int curr = topleft.getX(); curr <= room.topright.getX(); curr = curr + 1) {
            Point point = new Point(curr, topleft.getY());
            if (world[point.getX()][point.getY()] != theme) {
                return true;
            }
        }
        for (int curr = room.bottomleft.getX(); curr <= bottomright.getX(); curr = curr + 1) {
            Point point = new Point(curr, bottomright.getY());
            if (world[point.getX()][point.getY()] != theme) {
                return true;
            }
        }
        for (int curr = room.bottomleft.getY(); curr <= topleft.getY(); curr = curr + 1) {
            Point point = new Point(topleft.getX(), curr);
            if (world[point.getX()][point.getY()] != theme) {
                return true;
            }
        }
        for (int curr = bottomright.getY(); curr <= room.topright.getY(); curr = curr + 1) {
            Point point = new Point(bottomright.getX(), curr);
            if (world[point.getX()][point.getY()] != theme) {
                return true;
            }
        }
        for (Room room1 : rooms) {
            if (room.bottomleft.getX() < room1.bottomleft.getX()
                    && room.bottomleft.getY() < room1.bottomleft.getY()
                    && room.topright.getX() > room1.topright.getX()
                    && room.topright.getY() > room1.topright.getY()) {
                return true;
            }
        }
        return false;
    }
    private void addRoom(Room room) {
        Point topleft = new Point(room.bottomleft.getX(), room.topright.getY());
        Point bottomright = new Point(room.topright.getX(), room.bottomleft.getY());
        for (int curr = topleft.getX() - 1; curr <= room.topright.getX() + 1; curr = curr + 1) {
            world[curr][topleft.getY() + 1] = Tileset.WALL;
            points.add(new Point(curr, topleft.getY()));
        }
        for (int curr = room.bottomleft.getX() - 1;
             curr <= bottomright.getX() + 1; curr = curr + 1) {
            world[curr][bottomright.getY() - 1] = Tileset.WALL;
            points.add(new Point(curr, bottomright.getY()));
        }
        for (int curr = room.bottomleft.getY() - 1; curr <= topleft.getY() + 1; curr = curr + 1) {
            world[topleft.getX() - 1][curr] = Tileset.WALL;
            points.add(new Point(topleft.getX(), curr));
        }
        for (int curr = bottomright.getY() - 1; curr <= room.topright.getY() + 1; curr = curr + 1) {
            world[bottomright.getX() + 1][curr] = Tileset.WALL;
            points.add(new Point(bottomright.getX(), curr));
        }
        for (int curr1 = topleft.getX(); curr1 <= bottomright.getX(); curr1 = curr1 + 1) {
            for (int curr2 = bottomright.getY(); curr2 <= topleft.getY(); curr2 = curr2 + 1) {
                world[curr1][curr2] = Tileset.FLOOR;
                points.add(new Point(curr1, curr2));
            }
        }
    }
    private void addHalls() {
        Room ref = rooms.get(0);
        for (Room room : rooms) {
            queue.add(room, Point.distance(ref.center, room.center));
        }
        while (queue.size() > 1) {
            Room reference = queue.removeSmallest();
            rooms.remove(reference);
            ArrayList<Point> centers = new ArrayList<>();
            for (Room room : rooms) {
                centers.add(room.center);
            }
            KDTree tree = new KDTree(centers);
            Point closeCenter = tree.nearest(reference.center.getX(), reference.center.getY());
            Point curr = reference.center;
            while (curr.getX() != closeCenter.getX()) {
                if (world[curr.getX()][curr.getY()] != Tileset.FLOOR) {
                    world[curr.getX()][curr.getY()] = Tileset.FLOOR;
                    world[curr.getX()][curr.getY() - 1] = Tileset.WALL;
                    world[curr.getX()][curr.getY() + 1] = Tileset.WALL;
                }
                if (curr.getX() < closeCenter.getX()) {
                    curr = new Point(curr.getX() + 1, curr.getY());
                } else {
                    curr = new Point(curr.getX() - 1, curr.getY());
                }
            }
            if (world[curr.getX()][curr.getY()] != Tileset.FLOOR) {
                if (reference.center.getX() < closeCenter.getX()) {
                    world[curr.getX()][curr.getY()] = Tileset.FLOOR;
                    world[curr.getX() + 1][curr.getY()] = Tileset.WALL;
                    world[curr.getX()][curr.getY() - 1] = Tileset.WALL;
                    world[curr.getX()][curr.getY() + 1] = Tileset.WALL;
                    world[curr.getX() + 1][curr.getY() - 1] = Tileset.WALL;
                    world[curr.getX() + 1][curr.getY() + 1] = Tileset.WALL;
                } else if (reference.center.getX() > closeCenter.getX()) {
                    world[curr.getX()][curr.getY()] = Tileset.FLOOR;
                    world[curr.getX() - 1][curr.getY()] = Tileset.WALL;
                    world[curr.getX()][curr.getY() - 1] = Tileset.WALL;
                    world[curr.getX()][curr.getY() + 1] = Tileset.WALL;
                    world[curr.getX() - 1][curr.getY() - 1] = Tileset.WALL;
                    world[curr.getX() - 1][curr.getY() + 1] = Tileset.WALL;
                }
            }
            while (curr.getY() != closeCenter.getY()) {
                if (world[curr.getX()][curr.getY()] != Tileset.FLOOR) {
                    world[curr.getX()][curr.getY()] = Tileset.FLOOR;
                    world[curr.getX() + 1][curr.getY()] = Tileset.WALL;
                    world[curr.getX() - 1][curr.getY()] = Tileset.WALL;
                }
                if (curr.getY() < closeCenter.getY()) {
                    curr = new Point(curr.getX(), curr.getY() + 1);
                } else {
                    curr = new Point(curr.getX(), curr.getY() - 1);
                }
            }
        }
    }
    public WorldGenerator(int width, int height, Integer seed, TETile theme, TETile newavatar) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        this.theme = theme;
        customavatar = newavatar;
        world = new TETile[width][height];
        random = new Random(seed);
        points = new HashSet<>();
        rooms = new ArrayList<>();
        queue = new DoubleMapPQ<>();
        for (int w = 0; w < width; w = w + 1) {
            for (int h = 0; h < height; h = h + 1) {
                world[w][h] = theme;
            }
        }
        while ((double) points.size() / (width * height) < ratio) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int maxdiff = (width + height) / 5;
            Point point1 = new Point(x, y);
            Point point2 = new Point(x - random.nextInt(maxdiff), y - random.nextInt(maxdiff));
            Room room = new Room(point1, point2);
            if (validRoom(room)) {
                addRoom(room);
                rooms.add(room);
            }
        }
        this.addHalls();
        while (avatar == null) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            if (world[x][y] == Tileset.FLOOR) {
                avatar = new Point(x, y);
                world[x][y] = customavatar;
            }
        }
    }

    public void changeAvatar(Point a) {
        if (world[a.getX()][a.getY()] == Tileset.FLOOR) {
            world[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
            world[a.getX()][a.getY()] = customavatar;
            avatar = a;
        }
    }

    public Point returnAvatar() {
        return avatar;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public boolean returnLineOfSight() {
        return lineofsight;
    }

    public String returnDirection() {
        return direction;
    }

    public int returnHeight() {
        return height;
    }

    public int returnWidth() {
        return width;
    }

    public static void main(String[] args) {
        WorldGenerator map = new WorldGenerator(80, 30, 2, Tileset.NOTHING, Tileset.AVATAR);
        TERenderer renderer = new TERenderer();
        renderer.initialize(80, 30);
        renderer.renderFrame(map.world);
    }
}
