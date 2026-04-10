package com.almirdev.simplerelics.common.effects;

import com.almirdev.simplerelics.common.RelicHolderContext;
import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.io.PacketHandler;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.util.NotificationUtil;

import java.awt.*;

public class NotificationEffect implements RelicEffect {
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(NotificationEffect.class);
    private final String titleKey;
    private final String subtitleKey;
    private final Color color;

    public NotificationEffect(String titleKey, String subtitleKey, Color color) {
        this.titleKey = titleKey;
        this.subtitleKey = subtitleKey;
        this.color = color;
    }

    @Override
    public void apply(RelicHolderContext context) {
        UUIDComponent uuidComponent = context.store().getComponent(context.getHolderRef(), UUIDComponent.getComponentType());
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
