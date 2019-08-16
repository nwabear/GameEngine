package com.nwabear.raycasting;

import java.io.File;
import java.util.Scanner;

public class AppContext {

    public static final int FPS = 50;
    public static final int FPS_DELAY_MILLIS = 1000 / FPS;

    public static final int WIDTH = 640;
    public static final int LENGTH = 400;

    public static final int TEX_SIZE = 64;

    public static final double MOVE_SPEED = 0.1;

    public static final int[][] MAP = loadFile();

    public static int[][] loadFile() {
        int[][] map = new int[5][5];
        try {
            Scanner scan = new Scanner(new File("pack/map.txt"));
            int lines = Integer.parseInt(scan.nextLine());
            map = new int[lines][lines];
            for(int x = 0; x < lines; x++) {
                String line = scan.nextLine();
                String[] letters = line.split(",");
                for(int y = 0; y < lines; y++) {
                    map[x][y] = Integer.parseInt(letters[y]);
                }
            }
            return map;
        } catch(Exception e) {
            System.out.println(e);
        }
        return map;
    }

    public static void main(String[] args) {
        int[][] test = loadFile();
        for(int x = 0; x < test.length; x++) {
            for(int y = 0; y < test[0].length; y++) {
                System.out.print(test[x][y] + ",");
            }
            System.out.println();
        }
    }
}
