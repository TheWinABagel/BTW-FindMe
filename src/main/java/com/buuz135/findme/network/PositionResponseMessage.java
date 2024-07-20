package com.buuz135.findme.network;


import com.buuz135.findme.FindMeConfig;
import com.buuz135.findme.client.ClientTickHandler;
import com.buuz135.findme.client.ParticlePosition;
import com.buuz135.findme.tracking.TrackingList;
import btw.world.util.BlockPos;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Packet250CustomPayload;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PositionResponseMessage implements FindmePacket {
    public static final String ID = "findme|PRS";
    private final List<BlockPos> positions;

    public PositionResponseMessage(List<BlockPos> positions) {
        this.positions = positions;
    }

    public PositionResponseMessage() {
        this.positions = new ArrayList<>();
    }

    public void read(DataInput input) throws IOException {
        int amount = input.readInt();
        while (amount > 0) {
            int x = input.readInt();
            int y = input.readInt();
            int z = input.readInt();
            positions.add(new BlockPos(x, y, z));
            --amount;
        }
        onPacketReceive();
    }

    public void write(DataOutput output) throws IOException {
        output.writeInt(positions.size());
        for (BlockPos position : positions) {
            output.writeInt(position.x);
            output.writeInt(position.y);
            output.writeInt(position.z);
        }
    }

    private void onPacketReceive() {
        Minecraft.getMinecraft().thePlayer.closeScreen();
        if (!positions.isEmpty()) {
            if (FindMeConfig.CONTAINER_TRACKING) {
                TrackingList.beginTracking();
                ClientTickHandler.addRunnable(TrackingList::clear, FindMeConfig.CONTAINER_TRACK_TIME);
            }
            for (BlockPos position : positions) {
                for (int i = 0; i < 2; ++i)
                    Minecraft.getMinecraft().effectRenderer.addEffect(new ParticlePosition(Minecraft.getMinecraft().thePlayer.worldObj, position.x + 0.75 - Minecraft.getMinecraft().thePlayer.worldObj.rand.nextDouble() / 2D, position.y + 0.75 - Minecraft.getMinecraft().thePlayer.worldObj.rand.nextDouble() / 2D, position.z + 0.75 - Minecraft.getMinecraft().thePlayer.worldObj.rand.nextDouble() / 2D));
            }
        }
    }

    @Override
    public Packet250CustomPayload toPacket() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        write(output);

        return new Packet250CustomPayload(ID, stream.toByteArray());
    }
}
