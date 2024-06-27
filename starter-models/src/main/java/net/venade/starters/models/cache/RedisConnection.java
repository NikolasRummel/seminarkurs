package net.venade.starters.models.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import lombok.Getter;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */

@Getter
public class RedisConnection {

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;

    public RedisConnection(String password, String host, int port, int db) {
        this.redisClient = RedisClient.create("redis://" + password + "@" + host + ":" + port + "/"+ db);
        this.connection = redisClient.connect();
    }

}
