package net.venade.starters.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nikolas Rummel
 * @since 09.09.2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private int uniqueId;
  private String firstName;
  private String lastName;
  private String userName;
  private String email;
  private String password;
  private String discordUserId;

}
