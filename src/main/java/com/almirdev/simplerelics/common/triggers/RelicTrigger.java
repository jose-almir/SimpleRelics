package com.almirdev.simplerelics.common.triggers;

import com.almirdev.simplerelics.common.RelicHolderContext;

public interface RelicTrigger {
    boolean shouldActivate(RelicHolderContext context);
}
