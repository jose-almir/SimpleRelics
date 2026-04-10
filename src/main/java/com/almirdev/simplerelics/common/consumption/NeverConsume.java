package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicHolderContext;

public class NeverConsume implements RelicConsumptionStrategy {
    @Override
    public void handleConsumption(RelicHolderContext context) {
        // no-op
    }
}