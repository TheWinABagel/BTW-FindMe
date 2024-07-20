package com.buuz135.findme;

import java.awt.*;

public class FindMeConfig {
    public static final FindMeConfig CLIENT = new FindMeConfig();
    public static int RADIUS_RANGE = 8;
    public static boolean CONTAINER_TRACKING = true;
    public static int CONTAINER_TRACK_TIME = 30 * 20;

    public static String CONTAINER_HIGHLIGHT_COLOR = "#cf9d15";
    private transient Color currentColor = null;


    public Color getColor() {
        if (currentColor == null) {
            try {
                currentColor = Color.decode(CONTAINER_HIGHLIGHT_COLOR.toLowerCase());
            } catch (NumberFormatException e) {
                //FindMe.LOG.error("Unable to parse color value '" + CONTAINER_HIGHLIGHT_COLOR.get() + "'", e);
                currentColor = Color.decode("#cf9d15");
            }
        }
        return currentColor;
    }
}
