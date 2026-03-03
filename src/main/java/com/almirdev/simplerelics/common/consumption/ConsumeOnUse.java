package com.almirdev.simplerelics.common.consumption;

import com.almirdev.simplerelics.common.RelicContext;

public class ConsumeOnUse implements RelicConsumptionStrategy {
    @Override
    public void handleConsumption(RelicContext context) {
        context.player()
                .getInventory()
                .getUtility()
                .removeItemStack(context.relicItem());
    }
}