package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicDamage;
import com.almirdev.simplerelics.common.RelicContext;
import com.almirdev.simplerelics.utils.RelicUtils;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.List;

public class VampiricEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(VampiricEffect.class);
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

        if (nearby.isEmpty()) return;

        LOGGER.atFine().log("Detected %d entities. Causing damage to all.", nearby.size());

        for (Ref<EntityStore> entityStoreRef : nearby) {
            Damage.EntitySource source = new Damage.EntitySource(context.ref());
            Damage damage = RelicDamage.Builder.create(source, damageAmount).withKnockback(
                    new Vector3d(0, 1, 0),
                    0.2f,
                    ChangeVelocityType.Add
            ).build();
            DamageSystems.executeDamage(entityStoreRef, context.buffer(), damage);
        }

        int healthIndex = DefaultEntityStatTypes.getHealth();
        float totalHeal = nearby.size() * damageAmount * lifeStealRatio;

        float missing = context.health().getMax() - context.health().get();
        float heal = Math.min(missing, totalHeal);

        LOGGER.atInfo().log("Restoring to player health: %f points", heal);
        context.entityStatMap().addStatValue(healthIndex, heal);

    }
}
