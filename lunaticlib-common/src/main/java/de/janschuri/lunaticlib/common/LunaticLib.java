package de.janschuri.lunaticlib.common;

import de.janschuri.lunaticlib.common.futurerequests.requests.*;
import de.janschuri.lunaticlib.common.utils.Utils;
import de.janschuri.lunaticlib.platform.Platform;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.common.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.common.utils.Mode;
import de.janschuri.lunaticlib.common.logger.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public final class LunaticLib {
    public static final String IDENTIFIER = "lunaticlib:futurerequests";
    static Mode mode = Mode.STANDALONE;
    private static boolean debug;
    static Platform platform;
    static Path dataDirectory;
    private static boolean installedGeyser;

    public static Platform getPlatform() {
        return platform;
    }

    public static Mode getMode() {
        return mode;
    }

    private static final FutureRequest[] requests = {
            new GetItemInMainHandRequest(),
            new GetNameRequest(),
            new GetPositionRequest(),
            new GiveItemDropRequest(),
            new HasItemInMainHandRequest(),
            new IsInRangeRequest(),
            new RemoveItemInMainHandRequest(),
            new GetSkinURLRequest(),
            new HasEnoughMoneyRequest(),
            new WithdrawMoneyRequest(),
    };

    static void registerRequests() {
        for (FutureRequest request : requests) {
            FutureRequestsHandler.registerRequest(request);
        }
    }

    public static void loadConfig() {

        File file = new File(dataDirectory.toFile(), "config.yml");

        if (!file.exists()) {
            return;
        }

        Yaml yaml = new Yaml();
        Map<String, Object> config;
        try {
            config = yaml.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        } catch (Exception e) {
            Logger.errorLog("Error loading config.yml");
            return;
        }

        LunaticLib.debug = (boolean) config.get("debug");
        if (LunaticLib.debug) {
            Logger.infoLog("Debug mode enabled.");
        }
    }

    public static void onEnable(Path dataDirectory, Mode mode, Platform platform) {
        LunaticLib.dataDirectory = dataDirectory;
        LunaticLib.mode = mode;
        LunaticLib.platform = platform;

        installedGeyser = Utils.classExists("org.geysermc.api.Geyser");

        loadConfig();
        registerRequests();
        Logger.infoLog("LunaticLib enabled.");
    }

    public static void onDisable() {
        FutureRequestsHandler.shutdown();
        Logger.infoLog("LunaticLib disabled.");
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isInstalledGeyser() {
        return installedGeyser;
    }
}
