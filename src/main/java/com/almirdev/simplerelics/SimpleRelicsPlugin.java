package com.almirdev.simplerelics;

import com.almirdev.simplerelics.common.Relics;
import com.almirdev.simplerelics.system.SimpleRelicsDamageSystem;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;


import javax.annotation.Nonnull;

public class SimpleRelicsPlugin extends JavaPlugin {

    public SimpleRelicsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Relics.registerAll();
        this.getEntityStoreRegistry().registerSystem(new SimpleRelicsDamageSystem());
    }
}