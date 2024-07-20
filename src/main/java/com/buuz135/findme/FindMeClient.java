package com.buuz135.findme;

import com.buuz135.findme.network.FindmePacket;
import com.buuz135.findme.network.PositionRequestMessage;
import emi.dev.emi.emi.api.stack.EmiIngredient;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.screen.EmiScreenManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Arrays;

@Environment(EnvType.CLIENT)
public class FindMeClient {
    public static KeyBinding KEY = new KeyBinding("key.findme.search", Keyboard.KEY_Y);
    public static ItemStack stack = null;

    public static void onKeyPressEvent() {
        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() == KEY.keyCode && Minecraft.getMinecraft().currentScreen != null) {
            GuiScreen screen = Minecraft.getMinecraft().currentScreen;
            if (stack != null) {
                sendToServer(new PositionRequestMessage(stack));
            }
            if (screen instanceof GuiContainer container) {
                EmiIngredient hov = EmiStack.EMPTY;
                if (!EmiScreenManager.isDisabled() && EmiScreenManager.getHoveredStack(Mouse.getEventX(), Mouse.getEventY(), false) instanceof EmiScreenManager.SidebarEmiStackInteraction sesi) {
                    hov = sesi.getStack();
                }
                if (!hov.isEmpty()) {
                    sendToServer(new PositionRequestMessage(hov.getEmiStacks().get(0).getItemStack()));
                } else {
                    Slot slot = container.getTheSlot();
                    if (slot != null) {
                        ItemStack stack = slot.getStack();
                        if (stack != null) {
                            sendToServer(new PositionRequestMessage(stack));
                        }
                    }
                }
            }
        }
    }

    private static void sendToServer(FindmePacket payload) {
        try {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(payload.toPacket());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
