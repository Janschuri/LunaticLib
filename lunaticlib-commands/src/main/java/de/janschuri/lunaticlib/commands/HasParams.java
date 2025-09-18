package de.janschuri.lunaticlib.commands;

import de.janschuri.lunaticlib.sender.PlayerSender;
import de.janschuri.lunaticlib.sender.Sender;
import net.kyori.adventure.text.Component;

import java.util.*;

public interface HasParams extends Command {

    List<Component> getParamsNames();

    default Component getParamsName(int paramIndex) {
        if (getParamsNames().size() <= paramIndex) {
            return Component.text("params");
        } else {
            return getParamsNames().get(paramIndex);
        }
    }

    default boolean isParam(int paramIndex, String arg) {
        return getParam(paramIndex).containsKey(arg);
    }

    List<Map<String, String>> getParams();

    default Map<String, String> getParam(int paramIndex) {
        if (paramIndex < getParams().size()) {
            return getParams().get(paramIndex);
        } else {
            return new HashMap<>();
        }
    }

    default List<Component> getFormattedParamsList(Sender sender, int paramIndex) {
        List<Component> list = new ArrayList<>();
        for (String param : getParam(paramIndex).keySet()) {
            if (sender.hasPermission(getParam(paramIndex).get(param))) {
                list.add(Component.text(param));
            }
        }
        return list;
    }

    default Map<String, String> getOnlinePlayersParam() {
        return getOnlinePlayersParam(this.getPermission());
    }

    default Map<String, String> getOnlinePlayersParam(String permission) {
        Collection<PlayerSender> players = LunaticCommandHandler.adapter().getOnlinePlayers();
        Map<String, String> playerParams = new HashMap<>();

        for (PlayerSender player : players) {
            playerParams.put(player.getName(), permission);
        }
        return playerParams;
    }

    default List<String> paramsTabComplete(Sender sender, String[] args) {
        List<String> list = new ArrayList<>();
        int paramsIndex = args.length - 2;
        String lastArg = args[args.length - 1];

        if (getParams().isEmpty()) {
            return list;
        }


        for (String s : getParam(paramsIndex).keySet()) {
            if (!sender.hasPermission(getParam(paramsIndex).get(s))) {
                continue;
            }

            if (lastArg.equalsIgnoreCase("")) {
                list.add(s);
            } else {
                if (s.toLowerCase().startsWith(lastArg.toLowerCase())) {
                    list.add(s);
                }
            }
        }

        return list;
    }

}
