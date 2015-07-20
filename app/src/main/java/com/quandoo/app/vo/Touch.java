package com.quandoo.app.vo;

/**
 * Represents a finger touch on device screen. Has the coordinates and pressure.
 * {@link #hashCode()} and {@link #equals(Object)} must be overridden to detect
 * touches to same places.
 * <p/>
 * Touches with same coordinates but different pressure are considered to be
 * equal.
 *
 * @author Purushotham
 */
public class Touch {

    private int x;
    private int y;

    public Touch(float x, float y) {
        this.x = Math.round(x);
        this.y = Math.round(y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Touch other = (Touch) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Touch [" + x + ":" + y + "]";
    }

}