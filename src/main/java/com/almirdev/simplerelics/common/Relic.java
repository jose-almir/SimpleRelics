package com.almirdev.simplerelics.common;

import com.almirdev.simplerelics.common.effects.RelicEffect;
import com.almirdev.simplerelics.common.triggers.RelicTrigger;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.util.List;

public record Relic(String id, RelicTrigger trigger, List<RelicEffect> effects) {
    public void tryActivate(RelicContext context, ItemStack stack) {
        if(!trigger.shouldActivate(context)) return;

        for(RelicEffect effect : effects) {
            effect.apply(context);
        }

        context.player()
                .getInventory()
                .getUtility()
                .removeItemStack(stack);
    }
}
