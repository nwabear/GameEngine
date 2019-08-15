package com.nwabear.raycasting;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Surface extends JPanel {

    private double posX = 3;
    private double posY = 3;

    private double dirX = -1;
    private double dirY = 0;

    private double planeX = 0;
    private double planeY = 0.66;

    private double rotSpeed = 0.05;

    private boolean stop = false;

    private ArrayList<Actions> actions = new ArrayList<>();

    private Frame frame;

    private int windowLength = AppContext.LENGTH;
    private int windowWidth = AppContext.WIDTH;

    private int[][] buffer;

    private int[][] map = AppContext.MAP;

    public Surface(Frame frame) {
        this.frame = frame;
        this.buffer = new int[this.windowLength][this.windowWidth];
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

            while (hit == 0)
            {
                //jump to next map square, OR in x-direction, OR in y-direction
                if (sideDistX < sideDistY)
                {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                }
                else
                {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                //Check if ray has hit a wall
                if (this.map[mapX][mapY] > 0) {
                    hit = 1;
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

                Color color;

                color = getColor(map[mapX][mapY]);

                if (side == 1) {
                    color = color.darker();
                }

                g2d.setColor(color);
                g2d.drawLine(x, drawStart, x, drawEnd);
            }
        }

        int drawX = AppContext.WIDTH / 50;
        int drawY = drawX;
        int increment = drawX;

        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, increment * 13, increment * 13);

        int floorX = (int)this.posX;
        int floorY = (int)this.posY;

        for(int x = floorX + 5; x >= floorX - 5; x--) {
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
