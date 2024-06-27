package net.venade.services.user.repository;

import net.venade.starters.httpserver.log.Logger;
import net.venade.starters.models.sql.DatabaseConnection;
import net.venade.starters.models.user.User;
import lombok.Getter;

import java.sql.ResultSet;

/**
 * @author Nikolas Rummel
 * @since 29.09.2021
 */
@Getter
public class UserRepository {

  private static final String SQL_CREATE =
      "CREATE TABLE IF NOT EXISTS users"
          + "("
          + " uniqueId INT NOT NULL AUTO_INCREMENT,"
          + " firstName varchar(255) NOT NULL,"
          + " lastName varchar(255) NOT NULL,"
          + " userName varchar(255) NOT NULL,"
          + " email varchar(255) NOT NULL,"
          + " password TEXT NOT NULL,"
          + " discordUserId varchar(255) NOT NULL,"
          + " PRIMARY KEY (uniqueId)"
          + ")";

  private DatabaseConnection databaseConnection;
  private Logger logger;

  public UserRepository() {
    this.databaseConnection =
        new DatabaseConnection(
            "142.132.176.89", "seminarkurs", "cubid", "hnVaYhby3Z7AAbSK", 3306);
    this.databaseConnection.connect();
    this.databaseConnection.queryUpdate(SQL_CREATE);
    this.logger = Logger.Factory.createLogger(UserRepository.class);
    this.logger.info("Started connection to sql server");
  }

  public void saveUser(User user) {
    this.databaseConnection.update(
        "INSERT INTO users (firstName, lastName, userName, email, password, discordUserId) VALUES ('"
            + user.getFirstName() + "', '"
            + user.getLastName() + "', '"
            + user.getUserName() + "', '"
            + user.getEmail() + "', '"
            + user.getPassword() + "', '"
            + user.getDiscordUserId() + "');");

    this.logger.info("Successfully registered a new user ("  + user.getUserName() + "/" + user.getFirstName() + " " + user.getLastName() + ")");
  }

  public boolean userExists(String email) {
    ResultSet resultSet =
        this.databaseConnection.asyncQuery("SELECT email FROM users WHERE email= '" + email + "'");
    try {
      if (resultSet.next()) {
        return resultSet.getString("email") != null;
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
