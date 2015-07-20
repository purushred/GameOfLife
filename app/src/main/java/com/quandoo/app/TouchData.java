package com.quandoo.app;

import com.quandoo.app.util.Utils;
import com.quandoo.app.vo.Touch;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles screen {@link Touch} events. Buffers them in {@link #unprocessed} set and provides
 * a {@link #flush()} operation to promote the touches to the {@link #processed}
 * set.
 * <p/>
 * {@link #processed} set gets cleaned up upon retrieval, so that it won't get
 * processed twice.
 * <p/>
 * This class should be thread safe.
 *
 * @author Purushotham
 */
public class TouchData {

    /**
     * Contains {@link Touch} events that were not yet processed (finger was not
     * yet released).
     */
    private Set<Touch> unprocessed = new HashSet<>();

    /**
     * Contains {@link Touch} events that are ready for pickup by the game
     * logic.
     */
    private Set<Touch> processed = new HashSet<>();

    /**
     * Builds and adds a new {@link Touch} event to the {@link #unprocessed}
     * event buffer.
     *
     * @param x        Event coordinate X.
     * @param y        Event coordinate Y.
     */
    public void addTouch(float x, float y) {
        synchronized (unprocessed) {
            unprocessed.add(new Touch(x, y));
        }
    }

    public void flush() {
        synchronized (unprocessed) {
            synchronized (processed) {
                processed = new HashSet<>(unprocessed);
            }
            unprocessed.clear();
        }
    }

    public Set<Touch> getUnprocessed() {
        synchronized (unprocessed) {
            return new HashSet<>(unprocessed);
        }
    }

    /**
     * Gets the latest {@link Touch} events that are ready for logic
     * manipulation.
     *
     * @return Set of {@link Touch} events.
     */
    public Set<Touch> getProcessed() {
        synchronized (processed) {
            Set<Touch> response = new HashSet<>(processed);
            processed.clear();
            return response;
        }
    }
}
