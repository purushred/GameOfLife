package com.quandoo.app.util;

import android.util.Log;

/**
 * Collection of static convenience methods to reduce the amount of code in our
 * game.
 *
 * @author Purushotham
 */
final public class Utils {

    // It's an utility class, so the constructor is private
    private Utils() {}

    /**
     * Sleeps for given milliseconds. Throws {@link RuntimeException} instead
     * of {@link InterruptedException}.
     *
     * @param millis Milliseconds to sleep.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException("Sleep interrupted", e);
        }
    }

    /**
     * Logs a debug message using Android logging facility.
     *
     * @param source  Source of the message. Provide "this".
     * @param message Message (will go to String.format).
     * @param args    Args for String.format.
     */
    public static void debug(Object source, String message, Object... args) {
        Log.e(source.getClass().getSimpleName(), String.format(message, args));
    }
}
