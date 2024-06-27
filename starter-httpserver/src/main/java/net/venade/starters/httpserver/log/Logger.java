package net.venade.starters.httpserver.log;

import net.venade.starters.httpserver.http.server.HttpConfig;
import net.venade.starters.httpserver.utils.Providers;

/**
 * @author Nikolas Rummel
 * @since 04.10.2021
 */
public class Logger {

  private Class<?> senderClass;

  private Logger(Class<?> senderClass) {
    this.senderClass = senderClass;
  }

  public void info(String message) {
    System.out.println(
        "["
            + Thread.currentThread().getName()
            + "] INFO "
            + senderClass.getName()
            + " - "
            + message);
  }

  public void error(String message) {
    System.err.println(
        "["
            + Thread.currentThread().getName()
            + "] ERROR "
            + senderClass.getName()
            + " - "
            + message);
  }

  public void sendBootMessage() {
    System.out.println(" " +
                "\u001B[36m     _____               _     _____ _____ _____ _____   \n" +
                "\u001B[36m     |  |  |___ ___ ___ _| |___| __  |   __|   __|_   _| \n" +
                "\u001B[36m     |  |  | -_|   | .'| . | -_|    -|   __|__   | | |   \n" +
                "\u001B[36m      \\___/|___|_|_|__,|___|___|__|__|_____|_____| |_|  \n" +
                "\u001B[0m                                                         \n" +
                "     An REST Framework by Nikolas Rummel                          \n");
  }

  public void debug(String message, String extra) {
    HttpConfig config = Providers.get(HttpConfig.class);
    if (!config.isDebugMode()) return;
    System.out.println(
        "["
            + Thread.currentThread().getName()
            + "] DEBUG "
            + senderClass.getName()
            + " - "
            + message
            + " "
            + extra);
  }

  public static class Factory {
    public static Logger createLogger(Class<?> senderClass) {
      return new Logger(senderClass);
    }
  }
}
