package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.protocol.SoundCategory;
import com.hypixel.hytale.server.core.asset.type.soundevent.config.SoundEvent;
import com.hypixel.hytale.server.core.modules.entity.EntityModule;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;

public class PlaySoundEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final String soundId;

    public PlaySoundEffect(String soundId) {
        this.soundId = soundId;
    }

    @Override
    public void apply(RelicContext context) {
        int index = SoundEvent.getAssetMap().getIndex(soundId);

        World world = context.player().getWorld();
        if (world == null) {
            LOGGER.atWarning().log("World is null.");
            return;
        }

        world.execute(() -> {
            TransformComponent transform = context.store().getComponent(context.ref(), EntityModule.get().getTransformComponentType());
            if (transform == null) return;
            SoundUtil.playSoundEvent3dToPlayer(context.ref(), index, SoundCategory.UI, transform.getPosition(), context.store());
        });
    }
}
