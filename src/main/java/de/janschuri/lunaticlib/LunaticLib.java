package de.janschuri.lunaticlib;

import de.janschuri.lunaticlib.futurerequests.FutureRequest;
import de.janschuri.lunaticlib.futurerequests.FutureRequestsHandler;
import de.janschuri.lunaticlib.futurerequests.requests.*;
import de.janschuri.lunaticlib.senders.AbstractSender;
import de.janschuri.lunaticlib.utils.Mode;
import de.janschuri.lunaticlib.utils.Platform;
import de.janschuri.lunaticlib.utils.logger.Logger;

import java.util.HashSet;
import java.util.Set;

public final class LunaticLib {

    public static final String IDENTIFIER = "lunaticlib:futurerequests";
    static Mode mode = Mode.STANDALONE;
    public static boolean isDebug = true;
    static Platform platform;
    public static boolean installedVault = false;

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
    };

    public static boolean sendPluginMessage(String serverName, byte[] message) {
        switch (platform) {
            case PAPER:
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
        switch (platform) {
            case PAPER:
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

    static void unregisterRequests() {
        for (FutureRequest request : requests) {
            FutureRequestsHandler.unregisterRequest(request.getRequestName());
        }
    }

    public static AbstractSender getRandomPlayerSender() {
        switch (platform) {
//            case PAPER:
//                return AbstractSender.getSender(PaperLunaticLib.getRandomPlayer());
//            case VELOCITY:
//                return AbstractSender.getSender(VelocityLunaticLib.getRandomPlayer());
//            case BUNGEE:
//                return AbstractSender.getSender(BungeeLunaticLib.getRandomPlayer());
            default:
                Logger.errorLog("Platform not supported");
                return null;
        }
    }

}
