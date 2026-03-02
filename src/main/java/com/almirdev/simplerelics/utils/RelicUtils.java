package com.almirdev.simplerelics.utils;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;

public class RelicUtils {
    private static final String DAMAGE_ASSET_KEY = "Command";
    public static List<Ref<EntityStore>> getPlayerNearbyEntities(RelicContext context, double radius) {
        TransformComponent playerTransform = context.buffer().getComponent(context.ref(), TransformComponent.getComponentType());
        ModelComponent modelComponent = context.buffer().getComponent(context.ref(), ModelComponent.getComponentType());

        if(playerTransform == null || modelComponent == null) return List.of();

        Vector3d playerPos = playerTransform.getPosition().clone().add(0, modelComponent.getModel().getEyeHeight(), 0);
        List<Ref<EntityStore>> nearby = new ArrayList<>();
        SpatialResource<Ref<EntityStore>, EntityStore> entitySpatialResource = context.buffer().getResource(EntityModule.get().getEntitySpatialResourceType());
        entitySpatialResource.getSpatialStructure().collect(playerPos, radius, nearby);

        return nearby;
    }

    public static void dispatchDamageEvent(RelicContext context, Ref<EntityStore> targetRef, float damageAmount) {
        Damage.EntitySource source = new Damage.EntitySource(context.ref());
        DamageCause cause = DamageCause.getAssetMap().getAsset(DAMAGE_ASSET_KEY);

        if(cause == null) return;

        Damage damage = new Damage(source, cause, damageAmount);
        DamageSystems.executeDamage(targetRef, context.buffer(), damage);
    }
}
