package io.github.lukeeff.version.v1_15;

import io.github.lukeeff.version.VersionHandler;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.reflect.FieldUtils;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Handler_1_15_R1 implements VersionHandler {

    /**
     * Set a player glowing using packets
     * TODO: Customize this
     * @param player target player
     */
    public void setGlowing(Player player) {
        final byte GLOWINGBITMASK = (byte) 0x40;
        PacketPlayOutEntityMetadata metadata = getPacket(player, GLOWINGBITMASK);
        sendPacket(metadata, player);
    }

    /**
     * Get a PacketPlayOutEntityMetadata object with a modified bitmask.
     * @param player target player.
     * @param BITMASK the bitmask of the entry field at index 0.
     * @return a PacketPlayOutEntityMetadata object reference with the set bitmask.
     */
    private PacketPlayOutEntityMetadata getPacket(Player player, final byte BITMASK) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final int entityID = player.getEntityId();
        final DataWatcher dataWatcher = entityPlayer.getDataWatcher();
        setDataWatcherByteBitMask(dataWatcher, BITMASK);
        return new PacketPlayOutEntityMetadata(entityID, dataWatcher, true);
    }

    /**
     * Setter for DataWatcherByteBitMask object. Sets the Bit mask.
     * @param dataWatcher the target DataWatcher object.
     * @param BITMASK the bitmask we are setting.
     */
    private void setDataWatcherByteBitMask(DataWatcher dataWatcher, final byte BITMASK) {
        final int BYTEINDEX = 0; //Wiki.vg shows us that index 0 brings us to the Byte type.
        DataWatcher.Item<Byte> byteItem = getEntriesField(dataWatcher).get(BYTEINDEX);
        byteItem.a(BITMASK); //Obfuscated NMS setter method
    }

    /**
     * Gets the entries field of a DataWatcher object.
     * @param dataWatcher the target DataWatcher object.
     * @return the reference to the private entries Map in the DataWatcher's instance from its class.
     */
    private Int2ObjectOpenHashMap<DataWatcher.Item<Byte>> getEntriesField(DataWatcher dataWatcher) {
        final String ENTRIES = "entries"; //Found in NMS source code DataWatcher class. Name of target field.
        try {
            return (Int2ObjectOpenHashMap<DataWatcher.Item<Byte>>)
                    FieldUtils.readDeclaredField(dataWatcher, ENTRIES, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a reference to a PlayerConnection object from the target player.
     * @param player the target player.
     * @return a reference to the PlayerConnection object of a target player.
     */
    private PlayerConnection getConnection(Player player) {
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    /**
     * Sends the PacketPlayOutEntityMetaData object to each player online.
     * @param entityMetadata the packet to be sent.
     * @param player the player that won't receive the packet.
     */
    private void sendPacket(PacketPlayOutEntityMetadata entityMetadata, Player player) {
        Bukkit.getOnlinePlayers().stream().filter(p -> !(p.getName().equals(player.getName())))
                .forEach(p -> getConnection(p).sendPacket(entityMetadata));
    }

}
