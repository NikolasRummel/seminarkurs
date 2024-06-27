package net.venade.starters.gameserver.minecraft;

import lombok.Getter;
import net.venade.starters.gameserver.AbstractGameServer;

/**
 * @author Nikolas Rummel
 * @since 16.04.22
 */
@Getter
public class MinecraftGameServer extends AbstractGameServer {

    private String motd;

    public MinecraftGameServer(String emailOfOwner, String serverName, int memory, int port,
        int slots, int onlineCount, String motd, String state) {

        super(emailOfOwner, serverName, memory, port, slots, onlineCount, state);
        this.motd = motd;
    }
}
