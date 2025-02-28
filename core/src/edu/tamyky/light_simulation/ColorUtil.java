package edu.tamyky.light_simulation;

import com.badlogic.gdx.graphics.Color;

public class ColorUtil {

    private static final double TRANSITION_COEFFICIENT = 1;

    public static Color transition(double height) {
        float value = (float) (height * TRANSITION_COEFFICIENT);
        if (value >= 255) {
            value = 255;
        }
        return new Color(value, value, value, 100);
    }
}
