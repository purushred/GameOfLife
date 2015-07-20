package com.quandoo.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.quandoo.app.task.TouchInputProcessTask;
import com.quandoo.app.util.State;
import com.quandoo.app.util.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {

    /**
     * We will need {@link GameContext} in our main {@link Activity} so that
     * we can control our game state.
     */
    private GameContext gameContext;

    /**
     * This will let us know if game is running already so we don't restart it
     * on canvas rotation.
     */
    private boolean gameRunning = false;
    private GameView gameView;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Create the SurfaceView driven GameView where we will be
        // drawing on
        gameView = (GameView) findViewById(R.id.gameView);

        // Get the instance of GameContext when main activity gets created.
        gameContext = GameContext.getInstance(gameView);

        // View will require our GameContext, as it will start the main loop
        // when the text view is ready to draw on.
        gameView.setGameContext(gameContext);

    }

    public void startGame() {
        if (!gameRunning) {
            executorService = Executors.newSingleThreadExecutor();
            executorService.submit(new TouchInputProcessTask(gameContext));
            gameRunning = true;
        }
    }

    /* The methods below will handle the game state. */

    @Override
    protected void onStop() {
        Utils.debug(this, "onStop()");
        super.onStop();
        gameContext.setState(State.STOPPED);

    }

    @Override
    protected void onPause() {
        Utils.debug(this, "onPause()");
        super.onPause();
        gameContext.setState(State.PAUSED);
    }

    @Override
    protected void onResume() {
        Utils.debug(this, "onResume()");
        super.onResume();
        gameContext.setState(State.RUNNING);
        gameView.postInvalidate();
    }

    public void onButtonClick(View view) {
        Button toggleButton = (Button) view;
        if ((toggleButton.getText()).equals("Start")) {
            gameContext.setState(State.RUNNING);
            startGame();
            toggleButton.setText("Stop");
            toggleButton.setBackgroundResource(R.color.red);
        } else {
            gameContext.setState(State.STOPPED);
            toggleButton.setText("Start");
            toggleButton.setBackgroundResource(R.color.green);
            gameRunning = false;
        }
    }
}
