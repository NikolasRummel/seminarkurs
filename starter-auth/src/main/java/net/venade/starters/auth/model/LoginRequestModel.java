package net.venade.starters.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */


@Data
@AllArgsConstructor
public class LoginRequestModel {

  private String email, password;
}
