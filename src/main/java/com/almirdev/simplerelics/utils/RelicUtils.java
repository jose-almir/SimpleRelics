package com.almirdev.simplerelics.utils;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.spatial.SpatialResource;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.ArrayList;
import java.util.List;

public class RelicUtils {
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
}
