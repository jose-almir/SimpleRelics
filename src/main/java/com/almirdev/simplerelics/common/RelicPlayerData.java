package com.almirdev.simplerelics.common;

import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class RelicPlayerData implements Component<EntityStore> {
    public static final BuilderCodec<RelicPlayerData> CODEC = BuilderCodec.builder(RelicPlayerData.class, RelicPlayerData::new)
            .append(
                  new KeyedCodec<>("RelicSwapLockUntil", Codec.INSTANT),
                    (data, value) -> data.relicSwapLockUntil = value,
                    data -> data.relicSwapLockUntil
            ).add()
            .append(
                    new KeyedCodec<>("RelicActivationLockUntil", Codec.INSTANT),
                    (data, value) -> data.relicActivationLockUntil = value,
                    data -> data.relicActivationLockUntil
            )
            .add()
            .build();

    private Instant relicSwapLockUntil;
    private Instant relicActivationLockUntil;

    public RelicPlayerData() {
        relicSwapLockUntil = Instant.EPOCH;
        relicActivationLockUntil = Instant.EPOCH;
    }

    public RelicPlayerData(RelicPlayerData instance) {
        relicSwapLockUntil = instance.relicSwapLockUntil;
        relicActivationLockUntil = instance.relicActivationLockUntil;
    }

    @NullableDecl
    @Override
    public Component<EntityStore> clone() {
        return new RelicPlayerData(this);
    }

    public boolean canSwapRelic(Instant now) {
        return !now.isBefore(relicSwapLockUntil);
    }

    public void lockSwap(Instant now, long durationInSeconds) {
        relicSwapLockUntil = now.plus(durationInSeconds, ChronoUnit.SECONDS);
    }

    public boolean canActivateRelic(Instant now) {
        return !now.isBefore(relicActivationLockUntil);
    }

    public void lockActivation(Instant now, long durationInSeconds) {
        relicActivationLockUntil = now.plus(durationInSeconds, ChronoUnit.SECONDS);
    }
}
