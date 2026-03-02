package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;

public class FallDamageTrigger implements RelicTrigger {
    @Override
    public boolean shouldActivate(RelicContext context) {
        return context.damage().getCause() == DamageCause.FALL;
    }
}
