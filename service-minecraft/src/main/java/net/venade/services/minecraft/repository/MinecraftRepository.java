package net.venade.services.minecraft.repository;

import net.venade.starters.gameserver.minecraft.MinecraftGameServer;
import net.venade.starters.httpserver.log.Logger;
import net.venade.starters.models.sql.DatabaseConnection;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * @author Nikolas Rummel
 * @since 07.03.2022
 */
@Getter
public class MinecraftRepository {

  private static final String SQL_CREATE =
      "CREATE TABLE IF NOT EXISTS server"
          + "("
          + " serverId INT NOT NULL AUTO_INCREMENT,"
          + " emailOfOwner varchar(255) NOT NULL,"
          + " serverName varchar(255) NOT NULL,"
          + " memory INT NOT NULL,"
          + " port INT NOT NULL,"
          + " motd varchar(255) NOT NULL,"
          + " state varchar(255) NOT NULL,"
          + " slots INT NOT NULL,"
          + " onlineCount INT NOT NULL,"
          + " PRIMARY KEY (serverId)"
          + ")";
  private DatabaseConnection databaseConnection;

  private Logger logger;

  public MinecraftRepository() {
    this.databaseConnection =
        new DatabaseConnection(
            "142.132.176.89", "seminarkurs", "cubid", "hnVaYhby3Z7AAbSK", 3306);
    this.databaseConnection.connect();
    this.databaseConnection.queryUpdate(SQL_CREATE);
    this.logger = Logger.Factory.createLogger(MinecraftRepository.class);

    this.logger.info("Startet connection to mariadb server");
  }

  public void saveServer(MinecraftGameServer gameServer) {
    this.databaseConnection.update(
        "INSERT INTO server (emailOfOwner, serverName, memory, port, slots, onlineCount, motd, state) VALUES ('"
            + gameServer.getEmailOfOwner()
            + "', '"
            + gameServer.getServerName()
            + "', '"
            + gameServer.getMemory()
            + "', '"
            + gameServer.getPort()
            + "', '"
            + gameServer.getSlots()
            + "', '"
            + gameServer.getOnlineCount()
            + "', '"
            + gameServer.getMotd()
            + "', '"
            + gameServer.getState()
            + "');");
    this.logger.info(
        "Successfully registered a new server ("
            + gameServer.getEmailOfOwner()
            + " -> "
            + gameServer.getServerName()
            + ")");
  }

  public MinecraftGameServer getMinecraftServerModel(String email) {
    ResultSet resultSet =
        this.databaseConnection.asyncQuery("SELECT * FROM server WHERE emailOfOwner= '" + email + "'");
    try {
      if (resultSet.next()) {
        MinecraftGameServer gameServer = new MinecraftGameServer(
            resultSet.getString("emailOfOwner"),
            resultSet.getString("serverName"),
            resultSet.getInt("memory"),
            resultSet.getInt("port"),
            resultSet.getInt("slots"),
            resultSet.getInt("onlineCount"),
            resultSet.getString("motd"),
            resultSet.getString("state")
        );
        return gameServer;
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

}
