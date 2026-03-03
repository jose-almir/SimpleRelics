package com.almirdev.simplerelics.common;

import com.almirdev.simplerelics.common.consumption.ConsumeOnUse;
import com.almirdev.simplerelics.common.consumption.NeverConsume;
import com.almirdev.simplerelics.common.effects.CancelDamageEffect;
import com.almirdev.simplerelics.common.effects.NotificationEffect;
import com.almirdev.simplerelics.common.effects.PlaySoundEffect;
import com.almirdev.simplerelics.common.effects.RestoreHealthEffect;
import com.almirdev.simplerelics.common.triggers.FallDamageTrigger;
import com.almirdev.simplerelics.common.triggers.FatalDamageTrigger;

import java.awt.*;
import java.util.List;

public class Relics {
    public static final Relic EMERALD_CROSS =
            new Relic(
                    "SimpleRelics_EmeraldCross",
                    new FatalDamageTrigger(),
                    List.of(
                            new CancelDamageEffect(),
                            new RestoreHealthEffect(0.5f),
                            new NotificationEffect("items.SimpleRelics_EmeraldCross.notification_title", "items.SimpleRelics_EmeraldCross.notification_subtitle", Color.GREEN),
                            new PlaySoundEffect("SFX_Divine_Respawn")
                    ),
                    new ConsumeOnUse()
            );

    public static final Relic TEST_RELIC =
            new Relic(
                    "SimpleRelics_TestRelic",
                    new FatalDamageTrigger(),
                    List.of(),
                    new NeverConsume()
            );

    public static final Relic LUCKY_HORSESHOE =
            new Relic(
                    "SimpleRelics_LuckyHorseshoe",
                    new FallDamageTrigger(),
                    List.of(
                            new CancelDamageEffect()
                    ),
                    new NeverConsume()
            );

    public static void registerAll() {
        RelicRegistry.register(TEST_RELIC);
        RelicRegistry.register(EMERALD_CROSS);
        RelicRegistry.register(LUCKY_HORSESHOE);
    }

    private Relics() {}
}
