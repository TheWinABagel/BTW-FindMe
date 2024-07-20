package com.buuz135.findme;

import btw.AddonHandler;
import btw.BTWAddon;
import com.buuz135.findme.network.PositionRequestMessage;
import com.buuz135.findme.network.PositionResponseMessage;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class FindMe extends BTWAddon {

    public FindMe() {
        super();
    }

    @Override
    public void initialize() {
        AddonHandler.logMessage(this.getName() + " Version " + this.getVersionString() + " Initializing...");
    }

    @Override
    public void preInitialize() {
        registerProperty("t", "t");
    }

    @Override
    public void handleConfigProperties(Map<String, String> propertyValues) {
        super.handleConfigProperties(propertyValues);
    }

    @Override
    public boolean serverCustomPacketReceived(NetServerHandler handler, Packet250CustomPayload packet) {
        if (packet.channel.equals(PositionRequestMessage.ID)) {
            PositionRequestMessage.onMessageReceive(handler, packet.data);
        }
        return false;
    }

    @Override
    public boolean clientCustomPacketReceived(Minecraft mcInstance, Packet250CustomPayload packet) {
        if (packet.channel.equals(PositionResponseMessage.ID)) {
            ByteArrayInputStream stream = new ByteArrayInputStream(packet.data);
            DataInputStream input = new DataInputStream(stream);
            try {
                new PositionResponseMessage().read(input);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}