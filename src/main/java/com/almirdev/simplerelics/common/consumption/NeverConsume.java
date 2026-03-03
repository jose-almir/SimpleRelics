package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicContext;

public class NeverConsume implements RelicConsumptionStrategy {
    @Override
    public void handleConsumption(RelicContext context) {
        // no-op
    }
}