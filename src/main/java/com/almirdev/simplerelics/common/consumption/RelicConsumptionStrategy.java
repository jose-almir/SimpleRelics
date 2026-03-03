package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicContext;

public interface RelicConsumptionStrategy {
    void handleConsumption(RelicContext context);
}
