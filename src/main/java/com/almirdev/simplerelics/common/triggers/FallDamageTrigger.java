package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicHolderContext;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;

public class FallDamageTrigger implements RelicTrigger {
    @Override
    public boolean shouldActivate(RelicHolderContext context) {
        return context.damage().getCause() == DamageCause.FALL;
    }
}
