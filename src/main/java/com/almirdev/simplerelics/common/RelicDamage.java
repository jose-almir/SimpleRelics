package com.almirdev.simplerelics.common;

import com.almirdev.simplerelics.utils.SimpleRelicsLog;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.ChangeVelocityType;
import com.hypixel.hytale.server.core.entity.knockback.KnockbackComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;

import java.util.Objects;

public class RelicDamage {
    public static final HytaleLogger LOGGER = SimpleRelicsLog.getLogger(RelicDamage.class);
    private static final String DAMAGE_ASSET_KEY = "Relic";

    public static DamageCause getRelicDamageAsset() {
        DamageCause cause = DamageCause.getAssetMap().getAsset(DAMAGE_ASSET_KEY);

        if(cause == null) {
            LOGGER.atSevere().log("No damage cause found in asset Map");
            throw new RuntimeException("No damage cause found in asset Map");
        }

        return cause;
    }

    public static boolean isRelicDamage(Damage damage) {
        DamageCause relicCause = getRelicDamageAsset();
        return Objects.equals(Objects.requireNonNull(damage.getCause()).getId(), relicCause.getId());
    }

    public static class Builder {
        private final Damage damage;

        private Builder(Damage.Source source, float amount) {
            this.damage = new Damage(source, getRelicDamageAsset(), amount);
        }

        public static Builder create(Damage.Source source, float amount) {
            return new Builder(source, amount);
        }

        public Builder withKnockback(Vector3d velocity, float duration, ChangeVelocityType velocityType) {
            KnockbackComponent knockback = new KnockbackComponent();
            knockback.setVelocity(velocity);
            knockback.setVelocityType(velocityType);
            knockback.setDuration(duration);

            damage.putMetaObject(Damage.KNOCKBACK_COMPONENT, knockback);

            return this;
        }

        public Damage build() {
            return damage;
        }
    }
}
