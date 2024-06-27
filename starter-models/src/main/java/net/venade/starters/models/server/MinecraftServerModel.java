package net.venade.starters.models.server;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 08.09.2021
 */
@AllArgsConstructor
@Data
public class MinecraftServerModel {

  private String emailOfOwner;
  private String serverName;

  private int memory;

  private int port;
  private String motd, state;
  private int slots, onlineCount;
  private ServerVersion serverVersion;

  public enum ServerVersion {
    V_1_8,
    V_1_12,
    V_1_16,
    V_1_17,
    V_1_18
  }
}
