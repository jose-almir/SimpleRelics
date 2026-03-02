package com.almirdev.simplerelics.system;

import com.almirdev.simplerelics.common.Relic;
import com.almirdev.simplerelics.common.RelicContext;
import com.almirdev.simplerelics.common.RelicRegistry;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.Set;

public class SimpleRelicsDamageSystem extends EntityEventSystem<EntityStore, Damage> {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final Set<Dependency<EntityStore>> DEPENDENCIES = Set.of(new SystemDependency<>(Order.BEFORE, DamageSystems.FallDamagePlayers.class));


    public SimpleRelicsDamageSystem() {
        super(Damage.class);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType());
    }

    @NullableDecl
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getGatherDamageGroup();
    }

    @NonNullDecl
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return DEPENDENCIES;
    }

    @Override
    public void handle(int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl Damage damage) {
        if (damage.isCancelled()) {
            LOGGER.atInfo().log("Skipping damage due to cancelled.");
            return;
        }

        Ref<EntityStore> ref = archetypeChunk.getReferenceTo(i);
        Player player = store.getComponent(ref, Player.getComponentType());
        EntityStatMap stats = store.getComponent(ref, EntityStatMap.getComponentType());

        if (player == null || stats == null) {
            LOGGER.atWarning().log("Player or EntityStatMap is null.");
            return;
        }

        ItemStack utilityItem = player.getInventory().getUtilityItem();
        if (utilityItem == null) {
            LOGGER.atInfo().log("Player is not equipping utility item.");
            return;
        }

        LOGGER.atInfo().log("Player is equipping utility item. Trying to activate effects.");
        Relic relic = RelicRegistry.get(utilityItem.getItemId());

        if(relic == null) return;

        RelicContext context = new RelicContext(player, damage, stats, store, ref, commandBuffer);

        relic.tryActivate(context, utilityItem);
    }
}
