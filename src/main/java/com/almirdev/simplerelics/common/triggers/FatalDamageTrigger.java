package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicContext;

public class FatalDamageTrigger implements RelicTrigger {
    @Override
    public boolean shouldActivate(RelicContext context) {
        return context.isFatal();
    }
}
