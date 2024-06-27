package net.venade.starters.models.cache;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nikolas Rummel
 * @since 17.10.2021
 */

@Data @AllArgsConstructor
public class RedisConfiguration {

    private String host, password;
    private int port, database;

}
