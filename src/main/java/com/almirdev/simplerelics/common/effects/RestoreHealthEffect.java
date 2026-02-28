package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;

public class RestoreHealthEffect implements RelicEffect {
    private final float percent;

    public RestoreHealthEffect(float amount) {
        this.percent = amount;
    }

    @Override
    public void apply(RelicContext context) {
        int healthIndex = DefaultEntityStatTypes.getHealth();
        context.entityStatMap().addStatValue(healthIndex, context.health().getMax() * percent);
    }
}
