package de.janschuri.lunaticlib.platform.paper.proxyrequests.sender;

import de.janschuri.lunaticlib.platform.paper.proxyrequests.external.GUIManager;
import de.janschuri.lunaticlib.proxyrequests.ProxyRequestsLogger;
import de.janschuri.lunaticlib.platform.paper.sender.PaperPlayerSender;
import de.janschuri.lunaticlib.proxyrequests.sender.ProxyRequestsPlayerSender;
import de.janschuri.lunaticlib.utils.DecisionMessage;
import de.janschuri.lunaticlib.utils.paper.ItemStackUtils;
import de.janschuri.lunaticlib.utils.paper.PaperUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PaperProxyRequestsPlayerSender extends PaperPlayerSender implements ProxyRequestsPlayerSender<Player, CommandSender> {

    private final Player player;

    public PaperProxyRequestsPlayerSender(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public double[] getPosition() {
        return new double[] {
                player.getLocation().getX(),
                player.getLocation().getY(),
                player.getLocation().getZ()
        };
    }

    @Override
    public String getSkinURL() {
        return player.getPlayerProfile().getTextures().getSkin().toString();
    }

    @Override
    public boolean hasItemInMainHand() {
        return !player.getInventory().getItemInMainHand().getType().isAir();
    }

    @Override
    public byte[] getItemInMainHand() {
        ItemStack item = player.getInventory().getItemInMainHand();
        return ItemStackUtils.serializeItemStack(item);
    }

    @Override
    public boolean removeItemInMainHand() {
        player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        return true;
    }

    @Override
    public boolean giveItemDrop(byte[] item) {
        ItemStack itemStack = ItemStackUtils.deserializeItemStack(item);

        if (itemStack == null) {
            return false;
        }

        Map<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);

        for (ItemStack overflowItem : overflow.values()) {
            player.getWorld().dropItem(player.getLocation(), overflowItem);
        }

        return true;
    }

    @Override
    public boolean isInRange(ProxyRequestsPlayerSender playerSender, double range) {
        if (range < 0) {
            return true;
        }

        PaperProxyRequestsPlayerSender proxyRequestsPlayerSender = (PaperProxyRequestsPlayerSender) playerSender;

        Player otherPlayer = proxyRequestsPlayerSender.player;

        if (player.getWorld() != otherPlayer.getWorld()) {
            return false;
        }

        Location location1= player.getLocation();
        Location location2 = otherPlayer.getLocation();

        return PaperUtils.isInRange(location1, location2, range);
    }

    @Override
    public boolean openDecisionGUI(DecisionMessage message) {
        if (!PaperUtils.classExists("de.janschuri.lunaticlib.inventorygui.handler.GUIManager")) {
            ProxyRequestsLogger.error("GUIManager not found. Is lunaticlib-inventorygui present?");
        }

        GUIManager.openDecisionGUI(message, player);

        return true;
    }
}
