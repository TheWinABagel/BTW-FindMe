package com.buuz135.findme.mixin;

import com.buuz135.findme.FindMeClient;
import net.minecraft.src.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreen.class)
public abstract class GuiScreenMixin {
	@Inject(at = @At("TAIL"), method = "handleKeyboardInput")
	private void init(CallbackInfo info) {
		FindMeClient.onKeyPressEvent();
	}

}
