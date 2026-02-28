package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicContext;

public interface RelicTrigger {
    boolean shouldActivate(RelicContext context);
}
