package de.janschuri.lunaticlib.nms.v1_20_R1;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerPacketListener;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public final class PacketHandlerImpl extends ChannelDuplexHandler implements de.janschuri.lunaticlib.nms.PacketHandler {

    private final JavaPlugin plugin;
    private final String PACKET_INJECTOR_ID = "lunaticlib:packet_handler";
    public static final Map<UUID, Predicate<Packet<?>>> PACKET_HANDLERS = new HashMap<>();

    private final Player p; // Store your target player

    public PacketHandlerImpl(JavaPlugin plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packetO) throws Exception {
        if (!(packetO instanceof Packet<?> packet)) { // Utilize Java 17 features for pattern matching; Only intercept Packet Data
            super.channelRead(ctx, packetO);
            return;
        }

        Predicate<Packet<?>> handler = PACKET_HANDLERS.get(p.getUniqueId());
        if (handler != null) new BukkitRunnable() {
            public void run() {
                boolean success = handler.test(packet); // Check to make sure that the predicate works
                if (success) PACKET_HANDLERS.remove(p.getUniqueId()); // If successful, remove the packet handler
            }
        }.runTask(plugin);

        super.channelRead(ctx, packetO); // Perform default actions done by the duplex handler
    }

    @Override
    public void addPacketInjector(JavaPlugin plugin, Player p) {
        ServerPlayer sp = ((CraftPlayer) p).getHandle();

        try {
            Field connection = ServerPacketListener.class.getDeclaredField("c");
            connection.setAccessible(true);
            Channel ch = ((Connection) connection.get(sp.connection)).channel;

            if (ch.pipeline().get(PACKET_INJECTOR_ID) != null) return;
            ch.pipeline().addAfter("decoder", PACKET_INJECTOR_ID, new PacketHandlerImpl(plugin, p));
        } catch (ReflectiveOperationException e) {
            // handle errors
        }
    }

    @Override
    public void removePacketInjector(Player p) {
        ServerPlayer sp = ((CraftPlayer) p).getHandle();

        try {
            Field connection = ServerPacketListener.class.getDeclaredField("c");
            connection.setAccessible(true);
            Channel ch = ((Connection) connection.get(sp.connection)).channel;

            if (ch.pipeline().get(PACKET_INJECTOR_ID) == null) return;
            ch.pipeline().remove(PACKET_INJECTOR_ID);
        } catch (ReflectiveOperationException e) {
            // handle errors
        }
    }

}