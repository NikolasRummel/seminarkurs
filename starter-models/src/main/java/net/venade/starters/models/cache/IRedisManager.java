package net.venade.starters.models.cache;

import io.lettuce.core.api.async.RedisAsyncCommands;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */

public interface IRedisManager {

    RedisConnection getRedisConnection();
    RedisAsyncCommands<String, String> async();
    RedisAsyncCommands<String, String> async(int db);

    void setObject(String key, Object object);
    void setObjectExpired(String key, Object o, long seconds);

    <E> String fromObject(E object);
    <E> E toObject(String json, Class<E> eClass);
    <E> E getObject(String key, Class<E> eClass);
}
