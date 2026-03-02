package com.almirdev.simplerelics.common;

import com.almirdev.simplerelics.common.effects.RelicEffect;
import com.almirdev.simplerelics.common.triggers.RelicTrigger;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.util.List;

public record Relic(String id, RelicTrigger trigger, List<RelicEffect> effects) {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public void tryActivate(RelicContext context, ItemStack stack) {
        if(!trigger.shouldActivate(context)) {
            LOGGER.atInfo().log("Trigger not activated");
            return;
        }

        LOGGER.atInfo().log("Activating relic effects: %d", effects.size());
        for(RelicEffect effect : effects) {
            effect.apply(context);
        }

        LOGGER.atInfo().log("Consuming relic item");
        context.player()
                .getInventory()
                .getUtility()
                .removeItemStack(stack);
    }
}
