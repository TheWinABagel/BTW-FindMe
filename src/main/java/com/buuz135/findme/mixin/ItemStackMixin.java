package com.buuz135.findme.mixin;

import com.buuz135.findme.FindMeClient;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getTooltip", at = @At("RETURN"))
    private void test(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfoReturnable<List> cir) {
        FindMeClient.stack = (ItemStack) (Object) this;
    }
}
