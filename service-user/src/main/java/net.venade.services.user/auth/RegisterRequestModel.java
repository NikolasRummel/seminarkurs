package net.venade.services.user.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */
@Data
@AllArgsConstructor
public class RegisterRequestModel {

  private String firstName, lastName, username, email, password;

}
