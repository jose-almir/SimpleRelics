package com.almirdev.simplerelics.common;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatValue;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

public class EmeraldCrossRelicInterceptor extends FatalDamageInterceptor {
    public static final String RELIC_ID = "SimpleRelics_EmeraldCross";

    public EmeraldCrossRelicInterceptor() {
        super(RELIC_ID);
    }

    @Override
    public FatalDamageResult apply(Player player, Damage damage, EntityStatValue health, Store<EntityStore> store, Ref<EntityStore> ref) {
        damage.setCancelled(true);

        return new FatalDamageResult(
                health.getMax() * 0.5f,
                "SFX_Divine_Respawn",
                "items.SimpleRelics_EmeraldCross.notification_title",
                "items.SimpleRelics_EmeraldCross.notification_subtitle"
        );
    }
}
