package net.venade.services.user.controller;

import net.venade.services.user.auth.RegisterRequestModel;
import net.venade.starters.auth.AuthProvider;
import net.venade.starters.auth.model.LoginRequestModel;
import net.venade.starters.httpserver.http.HttpMethod;
import net.venade.starters.httpserver.http.HttpRequest;
import net.venade.starters.httpserver.http.HttpResponse;
import net.venade.starters.httpserver.http.HttpStatus;
import net.venade.starters.httpserver.http.annotation.HttpController;
import net.venade.starters.httpserver.http.annotation.HttpMapping;
import net.venade.starters.httpserver.log.Logger;
import net.venade.starters.models.ServiceRegistry;
import net.venade.starters.models.user.User;
import net.venade.services.user.repository.UserRepository;

import java.sql.ResultSet;
import java.util.Objects;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */
@HttpController
public class UserController {

  private final UserRepository repository = ServiceRegistry.getProvider(UserRepository.class);
  private final AuthProvider authProvider = ServiceRegistry.getProvider(AuthProvider.class);

  @HttpMapping(path = "/user/register/", method = HttpMethod.POST)
  public String registerUser(HttpRequest request, HttpResponse response) {
    RegisterRequestModel registerRequestModel = request.getBodyAsObject(RegisterRequestModel.class);

    if (repository.userExists(registerRequestModel.getEmail())) {
      response.setStatusCode(HttpStatus.FORBIDDEN);
      return "This email is taken!";
    }

    User user =
        new User(
            1,
            registerRequestModel.getFirstName(),
            registerRequestModel.getLastName(),
            registerRequestModel.getUsername(),
            registerRequestModel.getEmail(),
            registerRequestModel.getPassword(),
            "example#0000");
    repository.saveUser(user);
    return "Successfully registered";
  }

  @HttpMapping(path = "/user/login/", method = HttpMethod.POST)
  public String loginUser(HttpRequest request, HttpResponse response) {
    LoginRequestModel loginRequestModel = request.getBodyAsObject(LoginRequestModel.class);

    if (checkLoginCredentials(loginRequestModel)) {
      String token = authProvider.authenticateUser(loginRequestModel);
      request.setToken(token);
      response.setStatusCode(HttpStatus.OK);

      return token;
    } else {
      response.setStatusCode(HttpStatus.UNAUTHORIZED);
      return "Wrong username or password";
    }
  }

  private boolean checkLoginCredentials(LoginRequestModel requestModel) {
    ResultSet resultSet =
        this.repository
            .getDatabaseConnection()
            .asyncQuery(
                "SELECT email, password FROM users WHERE email= '" + requestModel.getEmail() + "'");
    try {
      if (resultSet.next()) {
        String password = resultSet.getString("password");
        return Objects.equals(requestModel.getPassword(), password);
      }
      return false;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }
}
