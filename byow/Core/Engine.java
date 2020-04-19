package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world; handles all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMenu();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'Q' || c == 'q') {
                    System.exit(0);
                }
                if (c == 'N' || c == 'n') {
                    newWorld();
                }
                if (c == 'L' || c == 'l') {
                    String saved = null;
                    try {
                        FileReader fr = new FileReader("savefile.txt");
                        BufferedReader br = new BufferedReader(fr);
                        saved = br.readLine();
                    } catch (IOException ioe) {
                        System.err.println("IOException: " + ioe.getMessage());
                    }
                    TETile theme;
                    displayTheme();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch == 'n' || ch == 'N' || ch == 'g' || ch == 'G'
                                    || ch == 'w' || ch == 'W' || ch == 'f' || ch == 'F') {
                                theme = returnTheme(ch);
                                break;
                            }
                        }
                    }
                    TETile avatar;
                    char icon;
                    Color color = null;
                    String name = "";
                    selectAvatarIcon();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            icon = StdDraw.nextKeyTyped();
                            break;
                        }
                    }
                    selectAvatarColor();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch == 'b' || ch == 'B' || ch == 'r' || ch == 'R'
                                    || ch == 'g' || ch == 'G' || ch == 'y' || ch == 'Y'
                                    || ch == 'w' || ch == 'W') {
                                color = returnColor(ch);
                                break;
                            }
                        }
                    }
                    selectName();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char cha = StdDraw.nextKeyTyped();
                            if (cha == '/') {
                                break;
                            }
                            name = name + cha;
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, name);
                            StdDraw.show();
                            StdDraw.setPenColor(Color.BLACK);
                            StdDraw.filledRectangle(WIDTH / 2, HEIGHT / 2 - 1, WIDTH / 2, 1);
                        }
                    }
                    avatar = new TETile(icon, color, Color.black, name);
                    playWithString(saved, theme, avatar);
                }
            }
        }
    }

    private void newWorld() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "Enter seed (press 'S' when finished)");
        StdDraw.show();
        String stringseed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5'
                        || c == '6' || c == '7' || c == '8' || c == '9' || c == '0') {
                    stringseed = stringseed + c;
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, stringseed);
                    StdDraw.show();
                    StdDraw.setPenColor(Color.BLACK);
                    StdDraw.filledRectangle(WIDTH / 2, HEIGHT / 2 - 2, WIDTH / 2, 2);
                }
                if (c == 's' || c == 'S') {
//                    long seed = Long.parseLong(stringseed);
                    TETile theme;
                    displayTheme();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch == 'n' || ch == 'N' || ch == 'g' || ch == 'G'
                                    || ch == 'w' || ch == 'W' || ch == 'f' || ch == 'F') {
                                theme = returnTheme(ch);
                                break;
                            }
                        }
                    }
                    TETile avatar;
                    char icon;
                    Color color;
                    String name = "";
                    selectAvatarIcon();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            icon = StdDraw.nextKeyTyped();
                            break;
                        }
                    }
                    selectAvatarColor();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch == 'b' || ch == 'B' || ch == 'r' || ch == 'R'
                                    || ch == 'g' || ch == 'G' || ch == 'y' || ch == 'Y'
                                    || ch == 'w' || ch == 'W') {
                                color = returnColor(ch);
                                break;
                            }
                        }
                    }
                    selectName();
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char cha = StdDraw.nextKeyTyped();
                            if (cha == '/') {
                                break;
                            }
                            name = name + cha;
                            StdDraw.setPenColor(Color.WHITE);
                            StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, name);
                            StdDraw.show();
                            StdDraw.setPenColor(Color.BLACK);
                            StdDraw.filledRectangle(WIDTH / 2, HEIGHT / 2 - 1, WIDTH / 2, 1);
                        }
                    }
                    avatar = new TETile(icon, color, Color.black, name);
                    WorldGenerator map = new WorldGenerator(WIDTH, HEIGHT,
                            stringseed.hashCode(), theme, avatar);
                    play(map, "n" + stringseed + "s");
                }
            }
        }
    }

    private void play(WorldGenerator map, String previous) {
        ter.initialize(WIDTH, HEIGHT);
        ter.renderWithText(map.getWorld(), map);
        String directions = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'W' || c == 'w') {
                    directions = directions + 'w';
                    Point avatar = map.returnAvatar();
                    map.changeAvatar(new Point(avatar.getX(), avatar.getY() + 1));
                    map.direction = "Up";
                }
                if (c == 'A' || c == 'a') {
                    directions = directions + 'a';
                    Point avatar = map.returnAvatar();
                    map.changeAvatar(new Point(avatar.getX() - 1, avatar.getY()));
                    map.direction = "Left";
                }
                if (c == 'S' || c == 's') {
                    directions = directions + 's';
                    Point avatar = map.returnAvatar();
                    map.changeAvatar(new Point(avatar.getX(), avatar.getY() - 1));
                    map.direction = "Down";
                }
                if (c == 'D' || c == 'd') {
                    directions = directions + 'd';
                    Point avatar = map.returnAvatar();
                    map.changeAvatar(new Point(avatar.getX() + 1, avatar.getY()));
                    map.direction = "Right";
                }
                if (c == 'F' || c == 'f') {
                    map.lineofsight = !map.lineofsight;
                }
                if (c == ':') {
                    while (true) {
                        if (StdDraw.hasNextKeyTyped()) {
                            if (StdDraw.nextKeyTyped() == 'q' || StdDraw.nextKeyTyped() == 'Q') {
                                previous = previous + directions;
                                try {
                                    FileWriter fw = new FileWriter("savefile.txt", false);
                                    fw.write(previous);
                                    fw.close();
                                } catch (IOException ioe) {
                                    System.err.println("IOException: " + ioe.getMessage());
                                }
                                System.exit(0);
                            }
                        }
                    }
                }
                ter.renderWithText(map.getWorld(), map);
            }
        }
    }

    private void displayMenu() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Quit (Q)");
        StdDraw.show();
    }

    private void displayTheme() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Select theme:");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Nothing (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 3, "Grass (G)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "Water (W)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 1, "Flower (F)");
        StdDraw.show();
    }

    private void selectAvatarIcon() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Select avatar icon (type any character)");
        StdDraw.show();
    }

    private void selectAvatarColor() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 6, "Select color:");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 4, "Blue (B)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 2, "Red (R)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Green (G)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Yellow (Y)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "White (W)");
        StdDraw.show();
    }

    private void selectName() {
        ter.initialize(WIDTH, HEIGHT);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 1, "What is your name? (type '/' when finished)");
        StdDraw.show();
    }

    private TETile returnTheme(char ch) {
        if (ch == 'n' || ch == 'N') {
            return Tileset.NOTHING;
        }
        if (ch == 'g' || ch == 'G') {
            return Tileset.GRASS;
        }
        if (ch == 'w' || ch == 'W') {
            return Tileset.WATER;
        } else {
            return Tileset.FLOWER;
        }
    }

    private Color returnColor(char ch) {
        if (ch == 'b' || ch == 'B') {
            return Color.blue;
        }
        if (ch == 'r' || ch == 'R') {
            return Color.red;
        }
        if (ch == 'g' || ch == 'G') {
            return Color.green;
        }
        if (ch == 'y' || ch == 'Y') {
            return Color.YELLOW;
        } else {
            return Color.WHITE;
        }
    }

    private void playWithString(String input, TETile theme, TETile avatar) {
        char front = input.charAt(0);
        String input2 = input;
        if (front == 'n' || front == 'N') {
            input = input.toLowerCase();
            int last = input.indexOf('s');
            String seedstring = input.substring(1, last);
//            long seed = Integer.parseInt(seedstring);
            WorldGenerator newWorld = new WorldGenerator(WIDTH, HEIGHT,
                    seedstring.hashCode(), theme, avatar);
            input = input.substring(last);
            if (input.charAt(0) == 's' || input.charAt(0) == 'S') {
                input = input.substring(1);
                while (input.length() != 0) {
                    controls(input, newWorld);
                    input = input.substring(1);
                }
            }
            play(newWorld, input2);
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        TETile[][] finalWorldFrame = null;
        char front = input.charAt(0);
        if (front == 'n' || front == 'N') {
            input = input.toLowerCase();
            int last = input.indexOf('s');
            String seedstring = input.substring(1, last);
//            long seed = Long.parseLong(seedstring);
            WorldGenerator newWorld = new WorldGenerator(WIDTH, HEIGHT,
                    (int) seedstring.hashCode(), Tileset.NOTHING, Tileset.AVATAR);
            input = input.substring(last);
            if (input.charAt(0) == 's' || input.charAt(0) == 'S') {
                input = input.substring(1);
                while (input.length() != 0) {
                    controls(input, newWorld);
                    if (input.charAt(0) == ':' && (input.charAt(1) == 'Q'
                            || input.charAt(1) == 'q')) {
                        int end = input2.indexOf(':');
                        String directions = input2.substring(0, end);
                        try {
                            FileWriter fw = new FileWriter("savefile.txt", false);
                            fw.write(directions);
                            fw.close();
                        } catch (IOException ioe) {
                            System.err.println("IOException: " + ioe.getMessage());
                        }
                    }
                    if (input.length() == 1) {
                        break;
                    }
                    input = input.substring(1);
                }
                finalWorldFrame = newWorld.getWorld();
            }
        } else if (front == 'l' || front == 'L') {
            input = input.substring(1);
            String saved = null;
            try {
                FileReader fr = new FileReader("savefile.txt");
                BufferedReader br = new BufferedReader(fr);
                saved = br.readLine();
            } catch (IOException ioe) {
                System.err.println("IOException: " + ioe.getMessage());
            }

            return interactWithInputString(saved + input);
        }
        return finalWorldFrame;
    }

    private void controls(String input, WorldGenerator newWorld) {
        if (input.charAt(0) == 'W' || input.charAt(0) == 'w') {
            Point above = new Point(newWorld.returnAvatar().getX(),
                    newWorld.returnAvatar().getY() + 1);
            newWorld.changeAvatar(above);
            newWorld.direction = "Up";
        }
        if (input.charAt(0) == 'A' || input.charAt(0) == 'a') {
            Point left = new Point(newWorld.returnAvatar().getX() - 1,
                    newWorld.returnAvatar().getY());
            newWorld.changeAvatar(left);
            newWorld.direction = "Left";
        }
        if (input.charAt(0) == 'S' || input.charAt(0) == 's') {
            Point down = new Point(newWorld.returnAvatar().getX(),
                    newWorld.returnAvatar().getY() - 1);
            newWorld.changeAvatar(down);
            newWorld.direction = "Down";
        }
        if (input.charAt(0) == 'D' || input.charAt(0) == 'd') {
            Point right = new Point(newWorld.returnAvatar().getX() + 1,
                    newWorld.returnAvatar().getY());
            newWorld.changeAvatar(right);
            newWorld.direction = "Right";
        }
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        TERenderer renderer = new TERenderer();
        renderer.initialize(80, 30);
//        renderer.renderWithText(engine.interactWithInputString("lw"));
//        renderer.renderWithText(engine.interactWithInputString
//                ("n4675535341198815800ssaswwssdsawwsw:q"));
//        renderer.renderWithText(engine.interactWithInputString
//                ("n8685092880139258324swawaasswwdadd"));
//        renderer.renderWithText(engine.interactWithInputString
//                ("n8685092880139258324swawaasswwdadd"));
        engine.interactWithKeyboard();

//        TETile[][] test = engine.interactWithInputString("n8685092880139258324swawaasswwdadd");
//        for (int i = 0; i < 100; i = i + 1) {
//            assertArrayEquals(test, engine.interactWithInputString("n868s"));
//        }
    }
}

//    public static void main(String[] args) {
//        TETile[][] test = interactWithInputString("n7742738610918953084s");
//        for (int i = 0; i < 100; i = i + 1) {
//            assertEquals(test, interactWithInputString("n7742738610918953084s"));
//        }
//    }
