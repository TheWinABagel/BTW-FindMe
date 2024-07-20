package com.buuz135.findme;

import java.awt.*;

public class FindMeConfig {
    public static final FindMeConfig CLIENT = new FindMeConfig();
    public static int RADIUS_RANGE = 8;
    public static boolean CONTAINER_TRACKING = true;
    public static int CONTAINER_TRACK_TIME = 30 * 20;
    public static int MAX_PARTICLE_AGE = 20 * 10;

//    public static String CONTAINER_HIGHLIGHT_COLOR = "#cf9d15";
    public transient Color currentColor = null;
    //2014641970

    public static int RED_COLOR = 20;
    public static int GREEN_COLOR = 255;
    public static int BLUE_COLOR = 50;
    public static int ALPHA_COLOR = 120;

    public Color getColor() {
        if (currentColor == null) {
//            try {
                currentColor = new Color(RED_COLOR, GREEN_COLOR, BLUE_COLOR, ALPHA_COLOR) /*Color.decode(CONTAINER_HIGHLIGHT_COLOR.toLowerCase())*/;
//            } catch (NumberFormatException e) {
//                //FindMe.LOG.error("Unable to parse color value '" + CONTAINER_HIGHLIGHT_COLOR.get() + "'", e);
//                currentColor = Color.decode("#7814FF32");
//            }
        }
        return currentColor;
    }
}
