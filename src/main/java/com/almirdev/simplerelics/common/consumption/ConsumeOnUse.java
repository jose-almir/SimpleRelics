package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicHolderContext;

public class ConsumeOnUse implements RelicConsumptionStrategy {
    @Override
    public void handleConsumption(RelicHolderContext context) {
        context.holder()
                .getInventory()
                .getUtility()
                .removeItemStack(context.relic());
    }
}