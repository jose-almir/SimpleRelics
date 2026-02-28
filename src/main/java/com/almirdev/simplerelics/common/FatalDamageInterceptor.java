package com.almirdev.simplerelics.common;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public abstract class FatalDamageInterceptor {
    protected String itemId;

    public FatalDamageInterceptor(String itemId) {
        this.itemId = itemId;
    }

    public abstract FatalDamageResult apply(Player player, Damage damage, EntityStatValue health, Store<EntityStore> store, Ref<EntityStore> ref);
}
