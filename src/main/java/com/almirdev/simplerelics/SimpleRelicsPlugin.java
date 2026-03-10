package com.almirdev.simplerelics;

import com.almirdev.simplerelics.common.RelicPlayerData;
import com.almirdev.simplerelics.common.Relics;
import com.almirdev.simplerelics.system.SimpleRelicsDamageSystem;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class SimpleRelicsPlugin extends JavaPlugin {
    private ComponentType<EntityStore, RelicPlayerData> relicPlayerDataComponent;
    public static SimpleRelicsPlugin instance;

    public SimpleRelicsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    protected void setup() {
        Level level = Level.parse(
                System.getProperty("simplerelics.logLevel", "WARNING")
        );
        SimpleRelicsLog.configure(level);
        Relics.registerAll();
        this.relicPlayerDataComponent = this.getEntityStoreRegistry().registerComponent(
                RelicPlayerData.class,
                "RelicPlayerDataComponent",
                RelicPlayerData.CODEC
        );
        this.getEntityStoreRegistry().registerSystem(new SimpleRelicsDamageSystem());
    }

    public ComponentType<EntityStore, RelicPlayerData> getRelicPlayerDataComponent() {
        return this.relicPlayerDataComponent;
    }
}