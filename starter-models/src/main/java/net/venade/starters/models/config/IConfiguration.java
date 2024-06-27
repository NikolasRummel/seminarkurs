package net.venade.starters.models.config;

import java.util.function.Supplier;

/**
 * @author Nikolas Rummel
 * @since 13.09.2021
 */
public interface IConfiguration<T> {

  IConfiguration<T> createIfNotExists(final Supplier<T> supplier);

  IConfiguration<T> load();

  IConfiguration<T> write(T type);

  IConfiguration<T> save();

  T get();
}
