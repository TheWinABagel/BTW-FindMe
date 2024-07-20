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
    private Map<String, String> props;

    public FindMe() {
        super();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void preInitialize() {
        this.modID = "findme";
        registerProperty("RadiusRange", String.valueOf(FindMeConfig.RADIUS_RANGE), "The inventory search radius. Large numbers might cause lag!");
        registerProperty("EnableContainerTracking", String.valueOf(FindMeConfig.CONTAINER_TRACKING), "When enabled, slots containing searched item will be highlighted.");
        registerProperty("ContainerTrackTime", String.valueOf(FindMeConfig.CONTAINER_TRACK_TIME), "The duration in ticks that containers will be tracked. Default is 30 seconds, or 600 ticks");
        registerProperty("ParticleSpawnTime", String.valueOf(FindMeConfig.MAX_PARTICLE_AGE), "The duration in ticks that particles will render in containers. Default is 10 seconds, or 200 ticks");

        registerProperty("ColorRed", String.valueOf(FindMeConfig.RED_COLOR), "RGB values and alpha (how visible it is) of the slot highlight. Valid numbers are from 0-255.");
        registerProperty("ColorGreen", String.valueOf(FindMeConfig.GREEN_COLOR));
        registerProperty("ColorBlue", String.valueOf(FindMeConfig.BLUE_COLOR));
        registerProperty("ColorAlpha", String.valueOf(FindMeConfig.ALPHA_COLOR));
    }

    @Override
    public void handleConfigProperties(Map<String, String> props) {
        this.props = props;
        try {
            FindMeConfig.RADIUS_RANGE = parseInt("RadiusRange");
            FindMeConfig.CONTAINER_TRACKING = Boolean.parseBoolean(props.get("EnableContainerTracking"));
            FindMeConfig.CONTAINER_TRACK_TIME = parseInt("ContainerTrackTime");
            FindMeConfig.MAX_PARTICLE_AGE = parseInt("ParticleSpawnTime");

            FindMeConfig.RED_COLOR = parseInt("ColorRed");
            FindMeConfig.GREEN_COLOR = parseInt("ColorGreen");
            FindMeConfig.BLUE_COLOR = parseInt("ColorBlue");
            FindMeConfig.ALPHA_COLOR = parseInt("ColorAlpha");
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private int parseInt(String val) throws NumberFormatException {
        return Integer.parseInt(props.get(val));
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