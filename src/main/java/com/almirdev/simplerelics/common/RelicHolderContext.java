package com.almirdev.simplerelics.common;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public record RelicHolderContext(
        Player holder,
        Damage damage,
        ItemStack relic,
        Store<EntityStore> store,
        CommandBuffer<EntityStore> buffer
) {
    public void cancelDamage() {
        damage.setCancelled(true);
    }

    public Ref<EntityStore> getHolderRef() {
        return holder.getReference();
    }

    public EntityStatValue getHolderHealthStat() {
        return getHolderStats().get(DefaultEntityStatTypes.getHealth());
    }

    public EntityStatMap getHolderStats() {
        return store.getComponent(getHolderRef(), EntityStatMap.getComponentType());
    }
}
