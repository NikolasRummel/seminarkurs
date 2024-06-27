package net.venade.starters.models;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nikolas Rummel
 * @since 05.10.2021
 */
public final class ServiceRegistry {

  private static Map<Class<?>, Object> singeltons = new HashMap<>();

  private ServiceRegistry() {}

  public static synchronized <T> T getProvider(Class<?> clazz, Object... parameters) {
    T object = (T) singeltons.get(clazz);
    if (object == null) {
      try {
        object = (T) clazz.getDeclaredConstructor().newInstance(parameters);

      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      singeltons.put(clazz, object);
    }
    return object;
  }

  public static synchronized <T> T getProvider(String className) {
    Class<?> clazz = null;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return getProvider(clazz);
  }

  public static synchronized <T> T putProvider(Class<?> clazz, Object object) {
    singeltons.put(clazz, object);
    return getProvider(clazz);
  }

  public static synchronized void removeProvider(Class<?> clazz) {
    singeltons.remove(clazz);
  }

  public static synchronized void destroy() {
    singeltons.clear();
  }
}
