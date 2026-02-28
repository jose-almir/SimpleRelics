package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;

public class CancelDamageEffect implements RelicEffect {
    @Override
    public void apply(RelicContext context) {
        context.damage().setCancelled(true);
    }
}
