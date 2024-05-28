package de.janschuri.lunaticlib.platform.bukkit.external;

import de.diddiz.LogBlock.Actor;
import de.janschuri.lunaticlib.common.LunaticLib;
import de.janschuri.lunaticlib.platform.bukkit.BukkitLunaticLib;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class LogBlock {

    private LogBlock() {
    }

    public static void logChestRemove(Entity entity, Block block, ItemStack itemStack) {
        if (!BukkitLunaticLib.isInstalledLogBlock()) {
            return;
        }
        de.diddiz.LogBlock.LogBlock.getInstance().getConsumer().queueChestAccess(Actor.actorFromEntity(entity), block.getState(), itemStack, true);
    }

    public static void logChestInsert(Entity entity, Block block, ItemStack itemStack) {
        if (!BukkitLunaticLib.isInstalledLogBlock()) {
            return;
        }
        de.diddiz.LogBlock.LogBlock.getInstance().getConsumer().queueChestAccess(Actor.actorFromEntity(entity), block.getState(), itemStack, false);
    }
}
