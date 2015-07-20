package com.quandoo.app.task;

import android.util.Log;

import com.quandoo.app.GameContext;
import com.quandoo.app.GameView;
import com.quandoo.app.Logic;
import com.quandoo.app.TouchData;
import com.quandoo.app.util.State;
import com.quandoo.app.util.Utils;

/**
 * Main task of the game. Controls the pulse.
 *
 * @author Purushotham
 */
public class TouchInputProcessTask implements Runnable {

    private final GameContext context;

    /**
     * Our desired frame duration.
     */
    public final static long MIN_TICK_TIME = 200;

    /**
     * Time when last update happened. Used for controlling the frame rate.
     */
    private long lastUpdate;


    public TouchInputProcessTask(GameContext context) {
        this.context = context;
    }

    /**
     * The main task happens here.
     *
     * @see #update()
     */
    @Override
    public void run() {
        Utils.debug(this, "Starting game loop");

        // The main loop will run until the game gets stopped.
        while (context.getState() != State.STOPPED) {

            // Handle game pause. Just sleep and wait till we're in a different
            // state.
            while (context.getState() == State.PAUSED) {
                Utils.sleep(100);
            }

            // Main update - see the method for details.
            update();
        }

        Utils.debug(this, "Stopping game loop");
    }

    /**
     * Game loop should process game aspects in following order:
     * 1. state
     * 2. input
     * 3. AI
     * 4. physics
     * 5. animations
     * 6. sound
     * 7. video
     * <p/>
     * State.
     * In our example user has no direct control over states, it is switched
     * when certain system events happen - view surface is ready, app is paused
     * or closed. state is handled in {@link #run()} method.
     * <p/>
     * TouchData.
     * All touch events are handled asynchronously when {@link GameView} is
     * touched. Everything goes to {@link TouchData} and it is used every time when
     * {@link Logic} ticks.
     * <p/>
     * AI physics.
     * All the AI and physics, if you can call it that, happens in
     * {@link Logic#tick()}.
     * <p/>
     * Animations & sound.
     * There are no animations in between the game states, and there is no
     * sound, so we have nothing here for that.
     * <p/>
     * Video.
     * Finally, video update happens in {@link Video#update()}.
     * <p/>
     * After everything runs, {@link #limitFPS()} ensures that the game does not
     * run too fast. Although if you won't make any optimizations, your game
     * update will not be faster than {@link #MIN_TICK_TIME}.
     */
    private void update() {
        try {
            // Process input and recalculate cells.
            context.getLogic().tick();

            // Draw new cell matrix on our game view.
            context.getGameView().postInvalidate();

            // Limits game speed on faster devices.
            limitFPS();

        } catch (Exception e) {
            Log.e(TouchInputProcessTask.class.getSimpleName(),
                    "Unexpected exception in main loop", e);
        }
    }

    /**
     * Counts time that passed since last game update and sleeps for a while if
     * this time was shorter than target frame time.
     */
    private void limitFPS() {
        long now = System.currentTimeMillis();
        if (lastUpdate > 0) {
            long delta = now - lastUpdate;
            if (delta < MIN_TICK_TIME) {
                Utils.sleep(MIN_TICK_TIME - delta);
            }
        }
        lastUpdate = System.currentTimeMillis();
    }
}
