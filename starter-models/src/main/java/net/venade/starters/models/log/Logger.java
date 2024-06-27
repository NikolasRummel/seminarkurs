package net.venade.starters.models.log;

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

  public static class Factory {
    public static Logger createLogger(Class<?> senderClass) {
      return new Logger(senderClass);
    }
  }
}
