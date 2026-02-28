package com.almirdev.simplerelics.common;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.Objects;

public record RelicContext(
        Player player,
        Damage damage,
        EntityStatMap entityStatMap,
        EntityStatValue health,
        Store<EntityStore> store,
        Ref<EntityStore> ref,
        CommandBuffer<EntityStore> buffer
) {
    public RelicContext(
            Player player,
            Damage damage,
            EntityStatMap stats,
            Store<EntityStore> store,
            Ref<EntityStore> ref,
            CommandBuffer<EntityStore> buffer
    ) {
        this(
                Objects.requireNonNull(player),
                Objects.requireNonNull(damage),
                Objects.requireNonNull(stats),
                Objects.requireNonNull(
                        stats.get(DefaultEntityStatTypes.getHealth()),
                        "Health stat missing"
                ),
                Objects.requireNonNull(store),
                Objects.requireNonNull(ref),
                Objects.requireNonNull(buffer)
        );
    }

    public float currentHealth() {
        return health.get();
    }

    public boolean isFatal() {
        return currentHealth() - damage.getAmount() <= 0;
    }
}
