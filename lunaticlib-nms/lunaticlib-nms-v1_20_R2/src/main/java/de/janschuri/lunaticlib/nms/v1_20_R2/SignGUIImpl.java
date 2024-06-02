package de.janschuri.lunaticlib.nms.v1_20_R2;

import de.janschuri.lunaticlib.nms.SignGUI;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class SignGUIImpl implements SignGUI {

    @Override
    public void sendSign(JavaPlugin plugin, Player p, Consumer<String[]> lines) {

        PacketHandlerImpl packetHandlerImpl = new PacketHandlerImpl(plugin, p);

        packetHandlerImpl.addPacketInjector(plugin, p); // Ensure a packet play is present

        Location l = p.getLocation();
        BlockPos pos = new BlockPos(l.getBlockX(), l.getBlockY()+3, l.getBlockZ()); // Create a sign GUI on the player
        BlockState old = ((CraftWorld) l.getWorld()).getHandle().getBlockState(pos); // Get the old block state for that position

        ClientboundBlockUpdatePacket sent1 = new ClientboundBlockUpdatePacket(pos, Blocks.OAK_SIGN.defaultBlockState());
        ((CraftPlayer) p).getHandle().connection.send(sent1); // Set that position to a sign

        ClientboundOpenSignEditorPacket sent2 = new ClientboundOpenSignEditorPacket(pos, true);
        ((CraftPlayer) p).getHandle().connection.send(sent2); // Open the sign editor

        PacketHandlerImpl.PACKET_HANDLERS.put(p.getUniqueId(), packetO -> {
            if (!(packetO instanceof ServerboundSignUpdatePacket packet)) return false; // Only intercept sign packets

            ClientboundBlockUpdatePacket sent3 = new ClientboundBlockUpdatePacket(pos, old);
            ((CraftPlayer) p).getHandle().connection.send(sent3); // Reset the block state for that packet

            lines.accept(packet.getLines()); // Accept the consumer here
            return true;
        });
    }
}
