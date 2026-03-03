package com.almirdev.simplerelics.utils;

import com.hypixel.hytale.logger.HytaleLogger;

import java.util.logging.Level;

public class SimpleRelicsLog {
    public static final HytaleLogger ROOT = HytaleLogger.get("SimpleRelics");

    private SimpleRelicsLog() {}

    public static void configure(Level level) {
        ROOT.setLevel(level);
    }

    public static HytaleLogger getLogger(Class<?> clazz) {
        HytaleLogger logger = ROOT.getSubLogger(clazz.getSimpleName());
        logger.setLevel(ROOT.getLevel());
        return logger;
    }
}
