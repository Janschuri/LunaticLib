package de.janschuri.lunaticlib.platform.bukkit.external;

import de.diddiz.LogBlock.Actor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public class LogBlock {

    private static de.diddiz.LogBlock.LogBlock logBlock;

    public LogBlock() {
        logBlock = (de.diddiz.LogBlock.LogBlock) Bukkit.getPluginManager().getPlugin("LogBlock");
    }

    public static void logChestRemove(Entity entity, Block block, ItemStack itemStack) {
        logBlock.getConsumer().queueChestAccess(Actor.actorFromEntity(entity), block.getState(), itemStack, true);
    }

    public static void logChestInsert(Entity entity, Block block, ItemStack itemStack) {
        logBlock.getConsumer().queueChestAccess(Actor.actorFromEntity(entity), block.getState(), itemStack, false);
    }
}
