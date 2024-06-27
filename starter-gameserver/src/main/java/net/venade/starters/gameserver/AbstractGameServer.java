package net.venade.starters.gameserver;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 16.04.22
 */
@Data
@AllArgsConstructor
public abstract class AbstractGameServer {

    protected String emailOfOwner;
    protected String serverName;

    protected int memory;

    protected int port;
    protected int slots, onlineCount;
    protected String state;

}
