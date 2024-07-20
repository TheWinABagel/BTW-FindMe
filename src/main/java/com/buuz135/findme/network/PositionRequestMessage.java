package com.buuz135.findme.network;

import com.buuz135.findme.FindMeConfig;
import com.buuz135.findme.tracking.TrackingList;
import btw.world.util.BlockPos;
import net.minecraft.src.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PositionRequestMessage implements FindmePacket {
    public static final String ID = "findme|PRQ";
    private ItemStack stack;

    public PositionRequestMessage(ItemStack stack) {
        this.stack = stack;
        TrackingList.trackItem(stack);
    }

    public static List<BlockPos> getBlockPosInAABB(AxisAlignedBB aabb) {
        List<BlockPos> blocks = new ArrayList<>();
        for (double y = aabb.minY; y < aabb.maxY; ++y) {
            for (double x = aabb.minX; x < aabb.maxX; ++x) {
                for (double z = aabb.minZ; z < aabb.maxZ; ++z) {
                    blocks.add(new BlockPos((int) x, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }

    public static ItemStack read(DataInput input) {
        try {
            return Packet.readItemStack(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void write(DataOutput output) throws IOException {
        Packet.writeItemStack(stack, output);
    }

    public static boolean onMessageReceive(NetServerHandler handler, byte[] buf) {
        EntityPlayer player = handler.playerEntity;
        ByteArrayInputStream stream = new ByteArrayInputStream(buf);
        DataInputStream input = new DataInputStream(stream);
        ItemStack stack = read(input);
        if (stack == null) {
            return false;
        }
        AxisAlignedBB box = new AxisAlignedBB(player.posX, player.posY, player.posZ, player.posX, player.posY, player.posZ).expand(FindMeConfig.RADIUS_RANGE, FindMeConfig.RADIUS_RANGE, FindMeConfig.RADIUS_RANGE);
        List<BlockPos> blockPosList = new ArrayList<>();
        for (BlockPos blockPos : getBlockPosInAABB(box)) {
            TileEntity tileEntity = handler.playerEntity.worldObj.getBlockTileEntity(blockPos.x, blockPos.y, blockPos.z);
            if (tileEntity != null) {
                if (tileEntity instanceof IInventory inventory) {
                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).isItemEqual(stack)) {
                            blockPosList.add(blockPos);
                            break;
                        }
                    }
                }

            }
        }
        if (!blockPosList.isEmpty()) {
            try {
                handler.netManager.addToSendQueue(new PositionResponseMessage(blockPosList).toPacket());
                return true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public Packet250CustomPayload toPacket() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        write(output);
        return new Packet250CustomPayload(ID, stream.toByteArray());
    }
}
