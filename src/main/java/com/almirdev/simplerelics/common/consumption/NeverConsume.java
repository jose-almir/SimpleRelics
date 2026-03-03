package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;

public class NeverConsume implements RelicConsumptionStrategy {
    @Override
    public void handleConsumption(RelicContext context) {
        // no-op
    }
}