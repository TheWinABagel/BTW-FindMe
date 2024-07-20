package com.buuz135.findme.network;

import net.minecraft.src.Packet250CustomPayload;

import java.io.IOException;

public interface FindmePacket {

    Packet250CustomPayload toPacket() throws IOException;
}
