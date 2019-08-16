package com.nwabear.raycasting;

import com.sun.webkit.dom.RGBColorImpl;
import org.w3c.dom.css.RGBColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

public class Surface extends JPanel {

    private double posX = 3;
    private double posY = 3;

    private double dirX = -1;
    private double dirY = 0;

    private double planeX = 0;
    private double planeY = 0.66;

    private double rotSpeed = 0.05;

    private ArrayList<Sprite> sprites = new ArrayList<>();

    private ArrayList<Actions> actions = new ArrayList<>();

    private Frame frame;

    private final int windowLength = AppContext.LENGTH;
    private final int windowWidth = AppContext.WIDTH;
    private int texSize;

    private Color[][] buffer;
    BufferedImage[] images;

    private int[][] map = AppContext.MAP;

    public Surface(Frame frame) {
        this.frame = frame;
        this.buffer = new Color[this.windowWidth][this.windowLength];
        for(int x = 0; x < windowWidth; x++) {
            for(int y = 0; y < windowLength; y++) {
                buffer[x][y] = Color.black;
            }
        }
        images = new BufferedImage[8];

        texSize = AppContext.TEX_SIZE;

        for(int i = 0; i < 8; i++) {
            images[i] = new BufferedImage(texSize, texSize, BufferedImage.TYPE_3BYTE_BGR);
        }

        try {
            images[0] = ImageIO.read(new File("pack/01.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[1] = ImageIO.read(new File("pack/02.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[2] = ImageIO.read(new File("pack/03.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[3] = ImageIO.read(new File("pack/04.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[4] = ImageIO.read(new File("pack/05.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[5] = ImageIO.read(new File("pack/06.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[6] = ImageIO.read(new File("pack/07.png"));
        } catch(Exception e) {
            // do nothing
        }

        try {
            images[7] = ImageIO.read(new File("pack/08.png"));
        } catch(Exception e) {
            // do nothing
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2d = (Graphics2D)(graphics);
        g2d.setColor(Color.BLACK);
        int w = AppContext.WIDTH;
        for(int x = 0; x < w; x++) {
            double cameraX = 2 * x / (double)(w) - 1;
            double rayDirX = dirX + planeX * cameraX;
            double rayDirY = dirY + planeY * cameraX;

            int mapX = (int)(posX);
            int mapY = (int)(posY);

            double sideDistX;
            double sideDistY;

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            int stepX;
            int stepY;

            int hit = 0;
            int side;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (posX - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - posX) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (posY - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - posY) * deltaDistY;
            }

            side = 0;

            while(hit == 0) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }

                if (this.map[mapX][mapY] > 0) {
                    hit = 1;
                }
            }

            if (side == 0) {
                perpWallDist = (mapX - posX + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (mapY - posY + (1 - stepY) / 2) / rayDirY;
            }

            int h = AppContext.LENGTH;

            int lineHeight = (int)(h / perpWallDist);

            int drawStart = -lineHeight / 2 + h / 2;
            if(drawStart < 0)drawStart = 0;
            int drawEnd = lineHeight / 2 + h / 2;
            if(drawEnd >= h)drawEnd = h - 1;

            int texNum = map[mapX][mapY] - 1;

            double wallX;
            if(side == 0)   wallX = posY + perpWallDist * rayDirY;
            else            wallX = posX + perpWallDist * rayDirX;
            wallX -= Math.floor((wallX));

            int texX = (int)(wallX * (double) texSize);
            if(side == 1 && rayDirX > 0) texX = texSize - texX - 1;
            if(side == 1 && rayDirY < 0) texX = texSize - texX - 1;

            for(int y = drawStart; y < drawEnd; y++) {
                int d = y * 256 - h * 128 + lineHeight * 128;
                int texY = ((d * texSize) / lineHeight) / 256;
                buffer[x][y] = (side == 1) ? new Color(images[texNum].getRGB(texX, texY)) : new Color(images[texNum].getRGB(texX, texY)).darker();
            }
        }

        for(int x = 0; x < windowWidth; x++) {
            for(int y = 0; y < windowLength; y++) {
                g2d.setColor(buffer[x][y]);
                g2d.drawLine(x, y, x, y);
                buffer[x][y] = Color.black;
            }
        }

        int drawX = AppContext.WIDTH / 50;
        int drawY = drawX;
        int increment = drawX;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, increment * 13, increment * 13);

        int floorX = (int)this.posX;
        int floorY = (int)this.posY;

        for(int x = floorX - 5; x <= floorX + 5; x++) {
            for(int y = floorY + 5; y >= floorY - 5; y--) {
                if(x != floorX || y != floorY) {
                    try {
                        g2d.setColor(getColor(map[x][y]));
                        if(map[x][y] == 0) {
                            g2d.setColor(Color.lightGray);
                        } else {
                            g2d.setColor(Color.darkGray);
                        }
                        g2d.fillRect(drawX, drawY, increment, increment);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        g2d.setColor(Color.black);
                        g2d.fillRect(drawX, drawY, increment, increment);
                    }
                } else {
                    g2d.setColor(Color.yellow);
                    g2d.fillRect(drawX, drawY, increment, increment);
                }
                drawY += increment;
            }
            drawX += increment;
            drawY = increment;
        }

        int num = (AppContext.WIDTH / 50) - 1;
        g2d.setColor(Color.gray);
        g2d.drawRect(num, num, drawX - increment, drawX - increment);
    }

    private void execute(int dir) {
        if(dir == 0 || dir == 1) {
            this.move(dir);
        } else if(dir == 2 || dir == 3) {
            this.rotate(dir);
        }
    }

    private void rotate(int dir) {
        if(dir == 2) {
            double oldDirX = dirX;
            dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
            dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
            planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
        } else if(dir == 3) {
            double oldDirX = dirX;
            dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
            dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
            planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
        }
    }

    private void move(int dir) {
        double moveSpeed = AppContext.MOVE_SPEED;
        if(dir == 0) {
            if(map[(int)(posX + dirX * moveSpeed)][(int)(posY)] == 0) posX += dirX * moveSpeed;
            if(map[(int)(posX)][(int)(posY + dirY * moveSpeed)] == 0) posY += dirY * moveSpeed;
        } else if(dir == 1) {
            if(map[(int)(posX - dirX * moveSpeed)][(int)(posY)] == 0) posX -= dirX * moveSpeed;
            if(map[(int)(posX)][(int)(posY - dirY * moveSpeed)] == 0) posY -= dirY * moveSpeed;
        }
    }

    public void tick() {
        this.repaint();
        this.actions = this.frame.getActions();
        for(Actions action : actions) {
            int toExc = 0;
            switch(action) {
                case UP:
                    toExc = 0;
                    break;

                case DOWN:
                    toExc = 1;
                    break;

                case LEFT:
                    toExc = 2;
                    break;

                case RIGHT:
                    toExc = 3;
                    break;
            }
            this.execute(toExc);
        }
    }

    private Color getColor(int num) {
        switch(num) {
            case 1:
                return Color.red;

            case 2:
                return Color.green;

            case 3:
                return Color.blue;

            case 4:
                return Color.white;

            default:
                return Color.black;
        }
    }
}
