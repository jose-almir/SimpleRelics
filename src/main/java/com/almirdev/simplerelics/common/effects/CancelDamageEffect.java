package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicHolderContext;

public class CancelDamageEffect implements RelicEffect {
    @Override
    public void apply(RelicHolderContext context) {
        context.cancelDamage();
    }
}
