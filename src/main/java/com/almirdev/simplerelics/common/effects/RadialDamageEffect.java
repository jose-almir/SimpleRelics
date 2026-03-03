package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.almirdev.simplerelics.utils.RelicUtils;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class RadialDamageEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(RadialDamageEffect.class);
    private final float radius;
    private final float damageAmount;

    public RadialDamageEffect(float radius, float damageAmount) {
        this.radius = radius;
        this.damageAmount = damageAmount;
    }

    @Override
    public void apply(RelicContext context) {
        List<Ref<EntityStore>> nearby = RelicUtils.getPlayerNearbyEntities(context, this.radius);
        LOGGER.atFine().log("There are %d entities. This entities will receive %f points of damage", nearby.size(), damageAmount);

        for (Ref<EntityStore> entityStoreRef : nearby) {
            RelicUtils.dispatchDamageEvent(context, entityStoreRef, damageAmount);
        }
    }
}
