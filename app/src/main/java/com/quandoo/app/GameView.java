package com.quandoo.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.quandoo.app.util.Utils;
import com.quandoo.app.vo.Cell;
import com.quandoo.app.vo.Touch;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The {@link View} which will hold our game visuals.
 *
 * @author Purushotham
 */
public class GameView extends TextView {

    private GameContext gameContext;

    /**
     * Defines background color.
     */
    private Paint bgPaint;

    /**
     * Defines active cell color.
     */
    private Paint cellPaint;

    /**
     * Defines color of cells that were drawn with finger but not yet flushed
     * into game logic.
     */
    private Paint prePaint;

    /**
     * Screen dimensions in pixels.
     */
    private int width, height;

    /**
     * Scale that tells how many screen pixels will represent one game pixel.
     */
    public static final float SCALE = 20f;

    public GameView(Context context) {
        super(context);
        initializeParams();
    }

    private void initializeParams() {
        bgPaint = new Paint();
        bgPaint.setColor(Color.WHITE);

        cellPaint = new Paint();
        cellPaint.setColor(Color.BLACK);

        prePaint = new Paint();
        prePaint.setColor(Color.GREEN);
        setFocusable(true);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeParams();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeParams();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // While finger is down on screen, we will gather input
        if (event.getAction() != MotionEvent.ACTION_UP) {
            // Adjust event coordinates according to our scale.
            gameContext.getTouchData().addTouch(
                    event.getX() / SCALE,
                    event.getY() / SCALE);
        } else {
            // When finger is released, input will be flushed into Logic.
            gameContext.getTouchData().flush();
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        updateUI(canvas);
    }

    /**
     * The view will need game context to pass itself to video renderer,
     * register new input events and start the main loop when canvas are ready.
     */
    public void setGameContext(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Utils.debug(this, "Size changed");
        setSize(w, h);
    }

    /**
     * Updates all the video:
     * 1. Clears background and fills it with {@link #bgPaint}.
     * 2. Draws {@link Cell} objects that come from {@link Logic}.
     * 3. Draws unprocessed cells that come from {@link TouchData}.
     *
     * @see #prepareBackground(Canvas)
     * @see #drawCells(Canvas)
     * @see #drawUnprocessedInput(Canvas)
     */
    public void updateUI(Canvas canvas) {
        prepareBackground(canvas);
        drawCells(canvas);
        drawUnprocessedInput(canvas);
    }

    /**
     * Fills given canvas with background color.
     */
    private void prepareBackground(Canvas canvas) {
        canvas.drawRect(new Rect(0, 0, width, height), bgPaint);
    }

    /**
     * Draws the current generation of cells that {@link Logic} provides.
     */
    private void drawCells(Canvas canvas) {
        drawCells(canvas, gameContext.getLogic().getCells(), cellPaint);
    }

    /**
     * Draws virtual cells to visualize unprocessed user input.
     */
    private void drawUnprocessedInput(Canvas canvas) {
        Collection<Cell> preview = new ArrayList<>();
        for (Touch touch : gameContext.getTouchData().getUnprocessed()) {
            preview.add(new Cell(touch.getX(), touch.getY()));
        }
        drawCells(canvas, preview, prePaint);
    }

    /**
     * Draws a collection of cells using given paint and canvas.
     * Cells are represented as a rectangle.
     *
     * @param canvas Canvas to draw on.
     * @param cells  Cells that should be drawn.
     * @param paint  Paint that defines cell color.
     * @see #SCALE
     */
    private void drawCells(Canvas canvas, Collection<Cell> cells, Paint paint) {
        for (Cell cell : cells) {
            Rect rect = new Rect(
                    Math.round(cell.getX() * SCALE),
                    Math.round(cell.getY() * SCALE),
                    Math.round(cell.getX() * SCALE + SCALE),
                    Math.round(cell.getY() * SCALE + SCALE));
            canvas.drawRect(rect, paint);
        }
    }

    /**
     * Changes video size. Will be called externally from our {@link GameView}.
     */
    public void setSize(int width, int height) {
        Utils.debug(this, "Setting video size: %d x %d", width, height);
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of our game matrix using scaled game pixels.
     *
     * @see #SCALE
     */
    public int getMatrixWidth() {
        return Math.round(width / SCALE);
    }

    /**
     * Gets the height of our game matrix using scaled game pixels.
     *
     * @see #SCALE
     */
    public int getMatrixHeight() {
        return Math.round(height / SCALE);
    }
}
