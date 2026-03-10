package com.almirdev.simplerelics.system;

import com.almirdev.simplerelics.SimpleRelicsPlugin;
import com.almirdev.simplerelics.common.*;
import com.almirdev.simplerelics.common.effects.ApplyEntityEffect;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.asset.type.entityeffect.config.OverlapBehavior;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.time.TimeResource;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.time.Instant;


public class SimpleRelicsDamageSystem extends EntityEventSystem<EntityStore, Damage> {
    public static final long RELIC_ACTIVATION_COOLDOWN_SECONDS = 6;
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(SimpleRelicsDamageSystem.class);

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

    @Override
    public void handle(int i, @NonNullDecl ArchetypeChunk<EntityStore> archetypeChunk, @NonNullDecl Store<EntityStore> store, @NonNullDecl CommandBuffer<EntityStore> commandBuffer, @NonNullDecl Damage damage) {
        if (damage.isCancelled()) {
            LOGGER.atInfo().log("Skipping damage due to cancelled.");
            return;
        }

        if(RelicDamage.isRelicDamage(damage)) {
            LOGGER.atInfo().log("Skipping damage due to relic damage.");
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

        Relic relic = RelicRegistry.get(utilityItem.getItemId());

        if(relic == null) {
            LOGGER.atInfo().log("Player is not using a relic.");
            return;
        }

        RelicPlayerData data = commandBuffer.ensureAndGetComponent(ref, SimpleRelicsPlugin.instance.getRelicPlayerDataComponent());
        TimeResource time = store.getResource(TimeResource.getResourceType());
        Instant now = time.getNow();

        if(!data.canActivateRelic(now)) {
            LOGGER.atInfo().log("Activation cooldown is active. Skipping...");
            return;
        }

        RelicContext context = new RelicContext(player, damage, utilityItem, stats, store, ref, commandBuffer);

        boolean hasActivated = relic.tryActivate(context);

        if(hasActivated) {
            LOGGER.atInfo().log("Activated relic. Locking activation for 6 seconds.");
            data.lockActivation(now, RELIC_ACTIVATION_COOLDOWN_SECONDS);
            ApplyEntityEffect.toSelf(
                    "Relic_Fatigue",
                    RELIC_ACTIVATION_COOLDOWN_SECONDS,
                    OverlapBehavior.OVERWRITE
            ).apply(context);
        }
    }
}
