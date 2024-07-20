package com.buuz135.findme.mixin;

import com.buuz135.findme.FindMeClient;
import com.buuz135.findme.FindMeConfig;
import com.buuz135.findme.tracking.TrackingList;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {

    @Shadow
    private Slot theSlot;
    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void findme$resetHoveredStack(int par1, int par2, float par3, CallbackInfo ci) {
        if (this.theSlot != null && !this.theSlot.getHasStack()) {
            FindMeClient.stack = null;
        }
    }

    @Inject(method = "drawSlotInventory", at = @At(value = "TAIL"))
    private void findme$setCurrentSlotColor(Slot slot, CallbackInfo ci) {
        if (slot != null && slot.getHasStack() && TrackingList.beingTracked(slot.getStack())) {
            Color c = FindMeConfig.CLIENT.getColor();

            GL11.glPushMatrix();
            GL11.glTranslated(0.0, 0.0, 300.0);
            int x = slot.xDisplayPosition - 1;
            int y = slot.yDisplayPosition - 1;
            Gui.drawRect(x, y, x + 18, y + 18, c.getRGB());
            GL11.glPopMatrix();
        }
    }
}
