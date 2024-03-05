package com.rustret;

public class Board {
    public static final int SIZE = 10;
    public static final int SHIPS_COUNT = 20;
    public static final int SHOT_ALREADY_EXIST = 0;
    public static final int SHOT_HIT = 1;
    public static final int SHOT_MISS = 2;
    public static final int SHOT_SHIP_DESTROYED = 3;
    private static final char CHAR_EMPTY = '·';
    private static final char CHAR_MISS = '×';
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
                    System.out.printf("%2c", ships[y][x] ? CHAR_SHIP : CHAR_MISS);
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

        if (lastShotMissed) {
            return SHOT_MISS;
        }

        locatedShips--;

        // Checking if hit destroyed the whole ship

        boolean vertical = ((y + 1 <= ships.length - 1) && ships[y + 1][x]) || ((y - 1 >= 0) && ships[y - 1][x]);

        int min = vertical ? y : x;
        while (true) {
            int temp = min - 1;
            if (temp < 0) {
                break;
            }
            if (vertical ? ships[temp][x] : ships[y][temp]) {
                min = temp;
            } else {
                break;
            }
        }

        int temp2 = min;
        boolean shipIsDestroyed = false;
        while (true) {
            if ((temp2 <= ships.length - 1) && (vertical ? ships[temp2][x] : ships[y][temp2])) {
                if (vertical ? hits[temp2][x] : hits[y][temp2]) {
                    temp2++;
                } else {
                    break;
                }
            } else {
                shipIsDestroyed = true;
                break;
            }
        }

        if (!shipIsDestroyed) {
            return SHOT_HIT;
        }

        // Adding misses around destroyed ship

        int temp3 = min - 1 >= 0 ? min - 1 : min;
        while (true) {
            if (temp3 > ships.length - 1) {
                break;
            }

            if (vertical) {
                for (int i = x - 1; i <= x + 1; i++) {
                    if (i >= 0 && i <= hits.length - 1) {
                        hits[temp3][i] = true;
                    }
                }

                if (!ships[temp3][x] && (temp3 != min - 1)) {
                    break;
                }
            } else {
                for (int i = y - 1; i <= y + 1; i++) {
                    if (i >= 0 && i <= hits.length - 1) {
                        hits[i][temp3] = true;
                    }
                }

                if (!ships[y][temp3]) {
                    break;
                }
            }

            temp3++;
        }

        return SHOT_SHIP_DESTROYED;
    }
}
