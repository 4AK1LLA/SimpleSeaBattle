package com.rustret;

public class Board {
    public static final int SIZE = 10;
    public static final int SHIPS_COUNT = 20;
    public static final int SHOT_ALREADY_EXIST = 0;
    public static final int SHOT_HIT = 1;
    public static final int SHOT_MISS = 2;
    private static final char CHAR_EMPTY = '·';
    private static final char CHAR_MISS = 'o';
    private static final char CHAR_HIT = '*';
    private static final char CHAR_SHIP = '□';
    public boolean[][] ships = new boolean[SIZE][SIZE];
    public boolean[][] hits = new boolean[SIZE][SIZE];
    public int locatedShips = 0;
    public boolean lastShotMissed;

    public void draw() {
        System.out.println("   Р Е С П У Б Л І К А");
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("%-2d", i);
            for (int j = 1; j <= SIZE; j++) {
                System.out.printf("%2c", ships[i - 1][j - 1] ? CHAR_SHIP : CHAR_EMPTY);
            }
            System.out.println();
        }
    }

    public void drawForEnemy() {
        System.out.println("   Р Е С П У Б Л І К А");
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("%-2d", i);
            for (int j = 1; j <= SIZE; j++) {
                int x = j - 1;
                int y = i - 1;
                if (!hits[y][x]) {
                    System.out.printf("%2c", CHAR_EMPTY);
                } else {
                    System.out.printf("%2c", ships[y][x] ? CHAR_HIT : CHAR_MISS);
                }
            }
            System.out.println();
        }
    }

    public boolean insertShip(int x, int y, int size, boolean vertical) {
        x--;
        y--;

        // Checking intersections with other ships and if inserted ship is not out of bounds
        int checkMinX = x - 1;
        int checkMinY = y - 1;
        int checkMaxX = vertical ? x + 1 : x + size;
        int checkMaxY = vertical ? y + size : y + 1;
        // System.out.printf("INFO: size: %d | x: %d | y: %d | minX: %d | maxX: %d | minY: %d | maxY: %d %n", size, x, y, checkMinX, checkMaxX, checkMinY, checkMaxY);
        for (int loopY = checkMinY; loopY <= checkMaxY; loopY++) {
            for (int loopX = checkMinX; loopX <= checkMaxX; loopX++) {
                try {
                    // System.out.print("CHECK: x: " + j + " | y: " + i);
                    // System.out.println(" Intersect: " + ships[i][j]);
                    if (ships[loopY][loopX]) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // System.out.println(" ArrayIndexOutOfBoundsException");
                    if (vertical && (loopX == x && loopY >= y && loopY <= checkMaxY - 1)
                            || !vertical && (loopY == y && loopX >= x && loopX <= checkMaxX - 1)) {
                        return false;
                    }
                }
            }
        }

        // Inserting ship
        locatedShips += size;
        for (int i = 1; i <= size; i++) {
            ships[y][x] = true;
            if (vertical) {
                y++;
            } else {
                x++;
            }
        }

        return true;
    }

    public int shot(int x, int y) {
        x--;
        y--;

        if (hits[y][x]) {
            return SHOT_ALREADY_EXIST;
        }
        hits[y][x] = true;

        lastShotMissed = !ships[y][x];
        return lastShotMissed ? SHOT_MISS : SHOT_HIT;
    }
}
