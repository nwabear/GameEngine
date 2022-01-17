package com.nwabear.mapmaker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MapMakerSurface extends JPanel {
    private Button[][] grid;
    private int[][] map;
    private ArrayList<BufferedImage> textures;

    private Point point;
    private int selection;

    private MapMakerFrame frame;

    private boolean wasClick = false;

    public MapMakerSurface(MapMakerFrame frame) throws Exception {
        this.frame = frame;
        this.textures = new ArrayList<>();
        this.grid = new Button[24][24];
        this.map = new int[20][20];
        this.loadTextures();
        for(int x = 0; x < 24; x++) {
            for(int y = 0; y < 24; y++) {
                this.grid[x][y] = new Button(textures.get(9), 0);
            }
        }
        this.setupMap();
        if(new File("pack/map.txt").exists()) {
            this.load(new File("pack/map.txt"));
        }
        this.drawMap();
        this.drawMenu();
    }

    private void loadTextures() throws Exception {
        this.textures.add(this.resize(ImageIO.read(new File("src/main/java/com/nwabear/mapmaker/resources/emptyGrid.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/01.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/02.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/03.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/04.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/05.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/06.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/07.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("pack/08.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("src/main/java/com/nwabear/mapmaker/resources/empty.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("src/main/java/com/nwabear/mapmaker/resources/start.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("src/main/java/com/nwabear/mapmaker/resources/exit.png"))));
        this.textures.add(this.resize(ImageIO.read(new File("src/main/java/com/nwabear/mapmaker/resources/save.png"))));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D) (graphics);

        if(this.wasClick) {
            this.point = this.frame.getPoint();
            int x = this.getPoint(this.point.x) / 32;
            int y = this.getPoint(this.point.y) / 32;

            switch(this.grid[x][y].getAction()) {
                case 1:
                    this.writeGrid(x, y);
                    break;
                case 2:
                    try {
                        this.save();
                    } catch(Exception e) {
                        // do nothing
                    }
                    break;
                case 3:
                    this.selection = y;
                    break;
                case 4:
                    this.frame.exit();
            }

            this.drawMap();
            this.wasClick = false;
        }
        this.drawFull(g2d);
    }

    private void drawMenu() {
        this.grid[23][0] = new Button(this.textures.get(11), 4);
        this.grid[22][0] = new Button(this.textures.get(12), 2);
        this.grid[0][0] = new Button(this.textures.get(0), 3);
        this.grid[0][1] = new Button(this.textures.get(1), 3);
        this.grid[0][2] = new Button(this.textures.get(2), 3);
        this.grid[0][3] = new Button(this.textures.get(3), 3);
        this.grid[0][4] = new Button(this.textures.get(4), 3);
        this.grid[0][5] = new Button(this.textures.get(5), 3);
        this.grid[0][6] = new Button(this.textures.get(6), 3);
        this.grid[0][7] = new Button(this.textures.get(7), 3);
        this.grid[0][8] = new Button(this.textures.get(8), 3);
        this.grid[0][10] = new Button(this.textures.get(10), 3);
    }

    private void writeGrid(int x, int y) {
        if(!(x == 4 || x == 23 || y == 4 || y == 23) || (this.selection != 0 && this.selection != 10)) {
            this.map[x - 4][y - 4] = this.selection;
        }

        if(this.selection == 10) {
            for(int x2 = 4; x2 < 24; x2++) {
                for(int y2 = 4; y2 < 24; y2++) {
                    if((x2 != x || y2 != y) && this.map[x2 - 4][y2 - 4] == 10) {
                        this.map[x2 - 4][y2 - 4] = 0;
                    }
                }
            }
        }
    }

    private void setupMap() {
        for(int x = 4; x < 24; x++) {
            for(int y = 4; y < 24; y++) {
                if (x == 4 || y == 4 || x == 23 || y == 23) {
                    this.grid[x][y] = new Button(textures.get(1), 1);
                    this.map[x - 4][y - 4] = 1;
                } else {
                    this.grid[x][y] = new Button(textures.get(0), 1);
                    this.map[x - 4][y - 4] = 0;
                }
            }
        }
    }

    private void drawMap() {
        for(int x = 4; x < 24; x++) {
            for(int y = 4; y < 24; y++) {
                this.grid[x][y].setImage(textures.get(this.map[x - 4][y - 4]));
            }
        }
    }

    private void drawFull(Graphics2D g2d) {
        for(int x = 0; x < 24; x++) {
            for(int y = 0; y < 24; y++) {
                g2d.drawImage(grid[x][y].getImage(), x * 32, y * 32, null);
            }
        }
    }

    private void save() throws Exception {
        File map = new File("pack/map.txt");
        if(map.createNewFile()) {
            System.out.println("File Created");
        }
        FileWriter fw = new FileWriter(map);
        fw.write("20\n");
        int startX = 1;
        int startY = 1;
        for(int x = 0; x < 20; x++) {
            for(int y = 0; y < 19; y++) {
                if(this.map[x][y] != 10) {
                    fw.write(this.map[x][y] + ",");
                } else {
                    startX = x;
                    startY = y;
                    fw.write(0 + ",");
                }
            }
            fw.write(this.map[x][19] + "\n");
        }
        fw.write("#" + startX + "," + startY);
        fw.close();
    }

    private void load(File map) throws Exception {
        Scanner scan = new Scanner(map);
        String line = scan.nextLine();
        int length = Integer.parseInt(line);
        for(int x = 0; x < length; x++) {
            line = scan.nextLine();
            for(int y = 0; y < length; y++) {
                this.map[x][y] = Integer.parseInt(line.split(",")[y]);
            }
        }
    }

    private BufferedImage resize(BufferedImage image) {
        Image temp = image.getScaledInstance(32, 32, Image.SCALE_REPLICATE);
        BufferedImage dimg = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(temp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private int getPoint(int num) {
        int toReturn = num / 32;
        return toReturn * 32;
    }

    public void tick() {
        this.wasClick = true;
        repaint();
    }
}
