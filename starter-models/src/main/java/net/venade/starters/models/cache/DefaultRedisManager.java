package net.venade.starters.models.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import io.lettuce.core.api.async.RedisAsyncCommands;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */

public class DefaultRedisManager implements IRedisManager {

    private final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, TypeAdapters.UUID).create();

    private RedisConnection redisConnection;
    private RedisAsyncCommands<String, String> async;

    public DefaultRedisManager(RedisConnection redisConnection) {
        this.redisConnection = redisConnection;
        this.async = redisConnection.getConnection().async();

    }

    @Override
    public void setObject(String key, Object object) {
        this.async.set(key, this.GSON.toJson(object));
    }

    @Override
    public void setObjectExpired(String key, Object o, long seconds) {
        this.setObject(key, o);
        this.async.expire(key, seconds);
    }

    @Override
    public <E> String fromObject(E object) {
        return this.GSON.toJson(object);
    }

    @Override
    public <E> E toObject(String json, Class<E> eClass) {
            if(json == null) return null;
            return this.GSON.fromJson(json, eClass);
    }

    @Override
    public <E> E getObject(String key, Class<E> eClass) {
        try {
            final String json = async.get(key).get();
            if(json == null) return null;

            return this.GSON.fromJson(json, eClass);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RedisConnection getRedisConnection() {
        return this.redisConnection;
    }

    @Override
    public RedisAsyncCommands<String, String> async() {
        this.async.select(1);
        return this.async;
    }

    @Override
    public RedisAsyncCommands<String, String> async(int db) {
        this.async.select(db);
        return this.async;
    }

}
