package net.venade.starters.models.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Nikolas Rummel
 * @since 13.09.2021
 */
public class JsonConfiguration<T> implements IConfiguration<T> {

  private final File file;
  private final Gson GSON =
      new GsonBuilder()
          .registerTypeAdapter(UUID.class, TypeAdapters.UUID)
          .disableHtmlEscaping()
          .setPrettyPrinting()
          .create();

  private final Class<T> tClass;
  private T document;

  public JsonConfiguration(final File file, final Class<T> tClass) {
    this.tClass = tClass;
    this.file = file;
  }

  @Override
  public IConfiguration<T> createIfNotExists(Supplier<T> supplier) {
    if (this.file == null || this.file.isDirectory())
      throw new IllegalArgumentException("File is null or directory");

    if (!this.file.exists()) {
      if (this.file.getParentFile() != null) {
        this.file.getParentFile().mkdirs();
      }
      try {
        this.file.createNewFile();
        this.write(supplier.get());
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
    return this;
  }

  @Override
  public IConfiguration<T> load() {
    try {
      final BufferedReader reader = new BufferedReader(new FileReader(this.file));
      this.document = this.GSON.fromJson(reader, this.tClass);
      reader.close();

    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return this;
  }

  @Override
  public IConfiguration<T> write(T document) {
    this.document = document;
    return this.save();
  }

  @Override
  public IConfiguration<T> save() {
    try {
      final Writer writer =
          new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8);

      this.GSON.toJson(this.document, writer);

      writer.flush();
      writer.close();

    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return this;
  }

  @Override
  public T get() {
    return this.document;
  }
}
