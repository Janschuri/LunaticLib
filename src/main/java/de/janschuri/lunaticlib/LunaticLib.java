package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.futurerequests.requests.*;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.logger.Logger;
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
    static boolean installedVault = false;
    static boolean installedLogBlock = false;
    static Path dataDirectory;

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
            new GetUniqueIdRequest(),
            new GiveItemDropRequest(),
            new HasItemInMainHandRequest(),
            new IsInRangeRequest(),
            new RemoveItemInMainHandRequest(),
            new GetSkinURLRequest(),
            new HasEnoughMoneyRequest(),
            new WithdrawMoneyRequest(),
    };

    public static boolean sendPluginMessage(String serverName, byte[] message) {
        Logger.debugLog("Sending plugin message to " + serverName);

        switch (platform) {
            case BUKKIT:
                return PaperLunaticLib.sendPluginMessage(message);
            case VELOCITY:
                return VelocityLunaticLib.sendPluginMessage(serverName, message);
            case BUNGEE:
                return BungeeLunaticLib.sendPluginMessage(serverName, message);
            default:
                Logger.errorLog("Platform not supported");
                return false;
        }
    }

    public static boolean sendPluginMessage(byte[] message) {
        Logger.debugLog("Sending plugin message to all servers.");

        switch (platform) {
            case BUKKIT:
                return PaperLunaticLib.sendPluginMessage(message);
            case VELOCITY:
                return VelocityLunaticLib.sendPluginMessage(message);
            case BUNGEE:
                return BungeeLunaticLib.sendPluginMessage(message);
            default:
                Logger.errorLog("Platform not supported");
                return false;
        }
    }

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
        Logger.infoLog("Debug mode enabled.");
    }

    static void onEnable() {
        loadConfig();
        registerRequests();
        Logger.infoLog("LunaticLib enabled.");
    }

    static void onDisable() {
        FutureRequestsHandler.shutdown();
        Logger.infoLog("LunaticLib disabled.");
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean isInstalledVault() {
        return installedVault;
    }
}
