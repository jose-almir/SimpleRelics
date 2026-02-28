package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicContext;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.NotificationUtil;

import java.awt.*;

public class NotificationEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private final String titleKey;
    private final String subtitleKey;
    private final Color color;

    public NotificationEffect(String titleKey, String subtitleKey, Color color) {
        this.titleKey = titleKey;
        this.subtitleKey = subtitleKey;
        this.color = color;
    }

    @Override
    public void apply(RelicContext context) {
        UUIDComponent uuidComponent = context.store().getComponent(context.ref(), UUIDComponent.getComponentType());
        if (uuidComponent == null) {
            LOGGER.atWarning().log("UUIDComponent is null.");
            return;
        }

        PlayerRef playerRef = Universe.get().getPlayer(uuidComponent.getUuid());
        if (playerRef == null) {
            LOGGER.atWarning().log("Player is null.");
            return;
        }

        PacketHandler packetHandler = playerRef.getPacketHandler();
        Message primaryMessage = Message.translation(titleKey).color(color);
        Message secondaryMessage = Message.translation(subtitleKey).color(Color.GRAY);

        NotificationUtil.sendNotification(
                packetHandler,
                primaryMessage,
                secondaryMessage
        );
    }
}
