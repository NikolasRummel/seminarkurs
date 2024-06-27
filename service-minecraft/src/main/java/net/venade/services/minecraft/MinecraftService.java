package net.venade.services.minecraft;

import net.venade.starters.httpserver.http.server.HttpConfig;
import net.venade.starters.httpserver.http.server.HttpServer;
import net.venade.starters.httpserver.utils.Providers;

/**
 * @author Nikolas Rummel
 * @since 07.03.2022
 */
public class MinecraftService {

  public static void main(String[] args) {
    new MinecraftService().start();
  }

  public void start() {
    Providers.put(HttpConfig.class, new HttpConfig().withPort(8083));
    HttpServer server = new HttpServer();
    server.start();
  }
}