package de.janschuri.lunaticlib.nms.interfaces;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.bukkit.entity.Player;

public interface PacketHandler {

    public void channelRead(ChannelHandlerContext ctx, Object packetO) throws Exception;

    public void addPacketInjector(Player p);

    public void removePacketInjector(Player p);

}
