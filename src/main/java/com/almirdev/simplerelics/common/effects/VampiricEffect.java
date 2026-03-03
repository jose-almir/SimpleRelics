package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.almirdev.simplerelics.utils.RelicUtils;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class VampiricEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final float radius;
    private final float damageAmount;
    private final float lifeStealRatio;

    public VampiricEffect(float radius, float damageAmount, float lifeStealRatio) {
        this.radius = radius;
        this.damageAmount = damageAmount;
        this.lifeStealRatio = lifeStealRatio;
    }

    @Override
    public void apply(RelicContext context) {
        List<Ref<EntityStore>> nearby = RelicUtils.getPlayerNearbyEntities(context, this.radius);

        if(nearby.isEmpty()) return;

        LOGGER.atInfo().log("Detected %d entities. Causing damage to all.");

        for (Ref<EntityStore> entityStoreRef : nearby) {
            RelicUtils.dispatchDamageEvent(context, entityStoreRef, damageAmount);
        }

        int healthIndex = DefaultEntityStatTypes.getHealth();
        float totalHeal = nearby.size() * damageAmount * lifeStealRatio;

        float missing = context.health().getMax() - context.health().get();
        float heal = Math.min(missing, totalHeal);

        LOGGER.atInfo().log("Restoring to player health: %f points", heal);
        context.entityStatMap().addStatValue(healthIndex, heal);

    }
}
