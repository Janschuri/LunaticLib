package de.janschuri.lunaticlib.nms;

import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface PacketHandler {

    public void channelRead(ChannelHandlerContext ctx, Object packetO) throws Exception;

    public void addPacketInjector(JavaPlugin plugin, Player p);

    public void removePacketInjector(Player p);

}