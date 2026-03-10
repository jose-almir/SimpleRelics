package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.almirdev.simplerelics.utils.RelicUtils;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.EntityEffect;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.effect.EffectControllerComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;


public class ApplyEntityEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(ApplyEntityEffect.class);
    private final String effectAssetKey;
    private final float duration;
    private final OverlapBehavior overlapBehavior;
    private final TargetMode targetMode;
    private final float radius;

    private ApplyEntityEffect(String effectAssetKey, float duration, OverlapBehavior overlapBehavior, TargetMode targetMode, float radius) {
        this.effectAssetKey = effectAssetKey;
        this.duration = duration;
        this.overlapBehavior = overlapBehavior;
        this.targetMode = targetMode;
        this.radius = radius;
    }

    public static ApplyEntityEffect toSelf(String effectAssetKey, float duration, OverlapBehavior overlapBehavior) {
        return new ApplyEntityEffect(effectAssetKey, duration, overlapBehavior, TargetMode.SELF, 0f);
    }

    public static ApplyEntityEffect toEnemies(String effectAssetKey, float duration, OverlapBehavior overlapBehavior, float radius) {
        return new ApplyEntityEffect(effectAssetKey, duration, overlapBehavior, TargetMode.ENEMIES, radius);
    }

    @Override
    public void apply(RelicContext context) {
        var effect = EntityEffect.getAssetMap().getAsset(effectAssetKey);

        if(effect == null) {
            LOGGER.atInfo().log("Effect not found for Asset Key: " + effectAssetKey);
            return;
        }

        switch (targetMode) {
            case SELF -> applyToEntity(context, context.ref());
            case ENEMIES -> {
                var nearby = RelicUtils.getPlayerNearbyEntities(context, radius);
                for(var ref : nearby) {
                    applyToEntity(context, ref);
                }
            }
        }
    }

    private void applyToEntity(RelicContext context, Ref<EntityStore> ref) {
        EffectControllerComponent controller = context.store().getComponent(ref, EffectControllerComponent.getComponentType());


        if(controller == null) {
            LOGGER.atInfo().log("EffectControllerComponent not found");
            return;
        }

        controller.addEffect(
                context.ref(),
                Objects.requireNonNull(EntityEffect.getAssetMap().getAsset(effectAssetKey)),
                duration,
                overlapBehavior,
                context.store()
        );
    }

    public enum TargetMode {
        SELF,
        ENEMIES
    }
}
