package com.nwabear.raycasting;

import static com.nwabear.raycasting.AppContext.FPS_DELAY_MILLIS;

public class GameClock implements Runnable {
    private Surface display;

    public GameClock(Surface display) {
        this.display = display;
    }

    @Override
    public void run() {
        while(true) {
            this.display.tick();
            try {
                Thread.sleep(FPS_DELAY_MILLIS);
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}