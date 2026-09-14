package com.buuz135.findme;

import api.BTWAddon;
import api.config.AddonConfig;
import com.buuz135.findme.network.PositionRequestMessage;
import com.buuz135.findme.network.PositionResponseMessage;
import net.minecraft.src.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class FindMe extends BTWAddon {

    @Override
    public void initialize() {
    }

    @Override
    public void registerConfigProperties(AddonConfig config) {
        config.registerInt("RadiusRange", FindMeConfig.RADIUS_RANGE, 0, 100, "The inventory search radius. Large numbers might cause lag!");
        config.registerBoolean("EnableContainerTracking", FindMeConfig.CONTAINER_TRACKING, "When enabled, slots containing searched item will be highlighted.");
        config.registerInt("ContainerTrackTime", FindMeConfig.CONTAINER_TRACK_TIME, 0, Integer.MAX_VALUE, "The duration in ticks that containers will be tracked. Default is 30 seconds, or 600 ticks");
        config.registerInt("ParticleSpawnTime", FindMeConfig.MAX_PARTICLE_AGE, 0, Integer.MAX_VALUE, "The duration in ticks that particles will render in containers. Default is 10 seconds, or 200 ticks");

        config.updatePath("ColorRed", "color.red");
        config.updatePath("ColorGreen", "color.green");
        config.updatePath("ColorBlue", "color.blue");
        config.updatePath("ColorAlpha", "color.alpha");

        config.registerInt("color.red", FindMeConfig.RED_COLOR, 0, 255, "Red value of the slot highlight.", "You can use https://rgbcolorpicker.com/ to find a color.");
        config.registerInt("color.green", FindMeConfig.GREEN_COLOR, 0, 255, "Green value of the slot highlight.", "You can use https://rgbcolorpicker.com/ to find a color.");
        config.registerInt("color.blue", FindMeConfig.BLUE_COLOR, 0, 255, "Blue value of the slot highlight.", "You can use https://rgbcolorpicker.com/ to find a color.");
        config.registerInt("color.alpha", FindMeConfig.ALPHA_COLOR, 0, 255, "Alpha value (how visible it is) of the slot highlight.", "You can use https://rgbcolorpicker.com/ to find a color.");
    }

    @Override
    public void handleConfigProperties(AddonConfig config) {
        FindMeConfig.RADIUS_RANGE = config.getInt("RadiusRange");
        FindMeConfig.CONTAINER_TRACKING = config.getBoolean("EnableContainerTracking");
        FindMeConfig.CONTAINER_TRACK_TIME = config.getInt("ContainerTrackTime");
        FindMeConfig.MAX_PARTICLE_AGE = config.getInt("ParticleSpawnTime");

        FindMeConfig.RED_COLOR = config.getInt("color.red");
        FindMeConfig.GREEN_COLOR = config.getInt("color.green");
        FindMeConfig.BLUE_COLOR = config.getInt("color.blue");
        FindMeConfig.ALPHA_COLOR = config.getInt("color.alpha");
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
                return true;
            }
        }
        return false;
    }
}