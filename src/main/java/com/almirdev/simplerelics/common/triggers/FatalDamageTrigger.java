package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicHolderContext;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;

public class FatalDamageTrigger implements RelicTrigger {
    @Override
    public boolean shouldActivate(RelicHolderContext context) {
        EntityStatValue playerHealth = context.getHolderHealthStat();
        return playerHealth.get() - context.damage().getAmount() <= 0;
    }
}
