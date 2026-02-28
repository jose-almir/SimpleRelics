package com.almirdev.simplerelics.common;

import java.util.*;

public final class RelicRegistry {

    private static final Map<String, Relic> RELICS = new HashMap<>();

    private RelicRegistry() {}

    public static void register(Relic relic) {
        Objects.requireNonNull(relic);

        if (RELICS.containsKey(relic.id())) {
            throw new IllegalStateException("Relic already registered: " + relic.id());
        }

        RELICS.put(relic.id(), relic);
    }

    public static Relic get(String id) {
        Relic relic = RELICS.get(id);
        if (relic == null) {
            throw new IllegalStateException("Unknown relic: " + id);
        }
        return relic;
    }

    public static Collection<Relic> all() {
        return Collections.unmodifiableCollection(RELICS.values());
    }
}
