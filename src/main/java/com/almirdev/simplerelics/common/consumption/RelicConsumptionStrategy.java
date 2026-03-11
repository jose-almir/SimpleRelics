package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicHolderContext;

public interface RelicConsumptionStrategy {
    void handleConsumption(RelicHolderContext context);
}
