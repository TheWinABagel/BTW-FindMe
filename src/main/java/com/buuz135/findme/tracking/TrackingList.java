package com.buuz135.findme.tracking;

import net.minecraft.src.ItemStack;

public class TrackingList {
    private static ItemStack stackB = null;
    private static ItemStack toTrack = null;

    public static boolean beingTracked(ItemStack stackA) {
        if (stackB == null) return false;
        return stackA.isItemEqual(stackB);
    }

    public static void clear() {
        stackB = null;
        toTrack = null;
    }

    public static void trackItem(ItemStack stack) {
        toTrack = stack.copy();
    }

    public static void beginTracking() {
        System.out.println("beginning tracking on " + toTrack);
        stackB = toTrack;
    }
}