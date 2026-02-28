package com.almirdev.simplerelics.system;

import com.almirdev.simplerelics.common.EmeraldCrossRelicInterceptor;
import com.almirdev.simplerelics.common.FatalDamageInterceptor;
import com.almirdev.simplerelics.common.FatalDamageResult;
import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.EntityEventSystem;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.NotificationUtil;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.awt.*;
import java.util.Map;

public class SimpleRelicsDamageSystem extends EntityEventSystem<EntityStore, Damage> {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static final Map<String, FatalDamageInterceptor> interceptors = Map.of(
            EmeraldCrossRelicInterceptor.RELIC_ID, new EmeraldCrossRelicInterceptor()
    );

    public SimpleRelicsDamageSystem() {
        super(Damage.class);
    }

    @NullableDecl
    @Override
    public Query<EntityStore> getQuery() {
        return Query.and(Player.getComponentType());
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

        EntityStatValue health = getHealthStat(stats);
        if (!isFatalDamage(health, damage)) {
            LOGGER.atInfo().log("Damage not fatal: %s", damage.getAmount());
            return;
        }

        ItemStack activeUtility = player.getInventory().getUtilityItem();

        if (activeUtility == null) {
            LOGGER.atWarning().log("There is no active utility item.");
            return;
        }

        FatalDamageInterceptor interceptor =
                interceptors.get(activeUtility.getItemId());

        if (interceptor == null) return;

        FatalDamageResult result = interceptor.apply(player, damage, health, store, ref);
        stats.addStatValue(getHealthIndex(), result.restoreAmount());
        player.getInventory().getUtility().removeItemStack(activeUtility);
        showNotification(store, ref, result);
        playSoundEffect(player, store, ref, result);
    }

    private EntityStatValue getHealthStat(EntityStatMap stats) {
        return stats.get(getHealthIndex());
    }

    private int getHealthIndex() {
        return DefaultEntityStatTypes.getHealth();
    }

    private boolean isFatalDamage(EntityStatValue health, Damage damage) {
        float remaining = health.get() - damage.getAmount();

        return remaining <= 0;
    }

    private void showNotification(Store<EntityStore> store, Ref<EntityStore> ref, FatalDamageResult result) {
        var uuidComponent = store.getComponent(ref, UUIDComponent.getComponentType());
        if (uuidComponent == null) {
            LOGGER.atWarning().log("UUIDComponent is null.");
            return;
        }

        var playerRef = Universe.get().getPlayer(uuidComponent.getUuid());
        if (playerRef == null) {
            LOGGER.atWarning().log("Player is null.");
            return;
        }

        var packetHandler = playerRef.getPacketHandler();
        var primaryMessage = Message.translation(result.notificationTitle()).color(Color.GREEN);
        var secondaryMessage = Message.translation(result.notificationSubtitle()).color(Color.GRAY);

        NotificationUtil.sendNotification(
                packetHandler,
                primaryMessage,
                secondaryMessage
        );
    }

    private void playSoundEffect(Player player, Store<EntityStore> store, Ref<EntityStore> ref, FatalDamageResult result) {
        int index = SoundEvent.getAssetMap().getIndex(result.soundId());
        World world = player.getWorld();
        if (world == null) {
            LOGGER.atWarning().log("World is null.");
            return;
        }

        world.execute(() -> {
            TransformComponent transform = store.getComponent(ref, EntityModule.get().getTransformComponentType());
            if (transform == null) return;
            SoundUtil.playSoundEvent3dToPlayer(ref, index, SoundCategory.UI, transform.getPosition(), store);
        });
    }
}
