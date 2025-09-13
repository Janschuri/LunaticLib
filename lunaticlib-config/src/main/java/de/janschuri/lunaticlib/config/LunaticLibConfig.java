package de.janschuri.lunaticlib.config;

import de.janschuri.lunaticlib.utils.Logger;
import de.janschuri.lunaticlib.utils.impl.LunaticLogger;


public class LunaticLibConfig {

    private LunaticLibConfig() {}

    static Logger logger = LunaticLogger.getLogger("LunaticLib-Config");

    public static void logger(Logger logger) {
        LunaticLibConfig.logger = logger;
    }
}
