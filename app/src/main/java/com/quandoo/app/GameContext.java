package com.quandoo.app;


import com.quandoo.app.task.TouchInputProcessTask;
import com.quandoo.app.util.State;
import com.quandoo.app.util.Utils;

/**
 * This is our game context. It links all game aspects together, different
 * components that have this context can fetch the required information
 * without having to talk to other components directly.
 * You can bring new features to the game and add them to this context, i.e.
 * if you wanted to have sound effects, add Sound class, register it here
 * and call required methods when certain events happen.
 *
 * @author Purushotham
 */
public class GameContext {

    /**
     * Singleton instance.
     *
     * @see #getInstance(GameView)
     */
    private static GameContext instance;

    private static GameView gameView;

    /**
     * The game state. {@link TouchInputProcessTask} switches it's logic according to it.
     */
    private State state;


    /**
     * Logic does all the calculations and controls where the cells are.
     */
    private final Logic logic;

    /**
     * Handles screen touches.
     */
    private final TouchData touchData;

    /**
     * Private constructor - use {@link #getInstance(GameView gView)} to get the game context.
     */
    private GameContext() {
        state = State.RUNNING;
        logic = new Logic(this);
        touchData = new TouchData();
    }

    /**
     * Singleton getter that returns the instance of GameContext.
     * We need a singleton here because when screen is rotated, Activity and
     * View gets destroyed and created again, but we have to retain the game
     * state.
     *
     * @param gView
     */
    public static GameContext getInstance(GameView gView) {
        if (instance == null) {
            instance = new GameContext();
            gameView = gView;
        }
        return instance;
    }

    /**
     * Changes the game state. Becomes effective in next main loop cycle.
     *
     * @param state New game {@link State}.
     */
    public void setState(State state) {
        Utils.debug(this, "Setting game state to %s", state);
        this.state = state;
    }

	/* Getters for various game aspects */

    public State getState() {
        return state;
    }

    public GameView getGameView() {
        return gameView;
    }

    public Logic getLogic() {
        return logic;
    }

    public TouchData getTouchData() {
        return touchData;
    }
}
