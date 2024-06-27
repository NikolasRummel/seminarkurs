package net.venade.services.user;

import net.venade.starters.httpserver.http.server.HttpServer;

/**
 * @author Nikolas Rummel
 * @since 08.09.2021
 */
public class UserService {

  public static void main(String[] args) {
    new UserService().start();
  }

  public void start() {
    HttpServer server = new HttpServer();
    server.start();
  }
}