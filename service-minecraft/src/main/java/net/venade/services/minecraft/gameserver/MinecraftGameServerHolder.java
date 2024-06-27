package net.venade.services.minecraft.gameserver;

import net.venade.starters.gameserver.minecraft.MinecraftGameServerProvider;
import net.venade.starters.models.ServiceRegistry;

/**
 * @author Nikolas Rummel
 * @since 18.04.22
 */
public class MinecraftGameServerHolder {

    private static MinecraftGameServerProvider serverProvider;

    public static MinecraftGameServerProvider getServerProvider() {
        if (serverProvider == null) {
            serverProvider = ServiceRegistry.putProvider(MinecraftGameServerProvider.class,
                MinecraftGameServerProvider.builder()
                    .withMemory(8192)
                    .build());
        }
        return serverProvider;
    }
}
