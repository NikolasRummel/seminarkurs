package net.venade.starters.auth;

import io.lettuce.core.RedisFuture;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import net.venade.starters.auth.model.LoginRequestModel;
import net.venade.starters.models.cache.DefaultRedisManager;
import net.venade.starters.models.cache.IRedisManager;
import net.venade.starters.models.cache.RedisConnection;
import net.venade.starters.models.cache.RedisDB;
import net.venade.starters.models.log.Logger;

/**
 *
 * @author Nikolas Rummel
 * @since 18.10.2021
 */
public class AuthProvider {

  private Logger logger = Logger.Factory.createLogger(AuthProvider.class);
  private IRedisManager redisManager;

  /**
   * Instantiates a new Auth provider.
   */
  public AuthProvider() {
    this.redisManager = new DefaultRedisManager(
            new RedisConnection("wuC4G5DWc6Pu9WTt0HelUsskD46PPabdgtT0TqdzXb7WzHiE6baSo38jBb04OTIIH5s7oeiyN5WEcCuPRuls7vs8EEN0zSkgk8FrvWU", "142.132.176.89", 6379, 10));
  }

  /**
   * Authenticate a new login.
   * Generates and saves a new Bearer token
   *
   * @param loginRequestModel the login request model
   * @return bearer token
   */
  public String authenticateUser(LoginRequestModel loginRequestModel) {
    final String token = this.generateNewToken();
    this.saveToken(token);

    this.logger.info(
        "Successfully authenticated " + loginRequestModel.getEmail() + " with token " + token);

    return token;
  }

  /**
   * Stores a token in the redis cache.
   *
   * @param token the token
   */
  private void saveToken(String token) {
    this.redisManager.async(RedisDB.AUTH).set("AUTH_TOKEN:" + token, "PLACEHOLDER");
    this.redisManager.async(RedisDB.AUTH).expire("AUTH_TOKEN:" + token, 60*60);
  }

  /**
   * Checks if a token is stored in the redis cache.
   *
   * @param token the token
   * @return the boolean
   */
  public boolean checkToken(String token) {
    RedisFuture<Long> future = this.redisManager.async(RedisDB.AUTH).exists("AUTH_TOKEN:" + token);
    try {
      long exists = future.get();
      return exists == 1L;
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Generates a new auth token.
   *
   * Output example:
   * wrYl_zl_8dLXaZul7GcfpqmDqr7jEnli
   * 7or_zct_ETxJnOa4ddaEzftNXbuvNSB-
   * CkZss7TdsTVHRHfqBMq_HqQUxBGCTgWj
   * 8loHzi27gJTO1xTqTd9SkJGYP8rYlNQn
   *
   * @return the generated token
   */
  private String generateNewToken() {
    final SecureRandom secureRandom = new SecureRandom();
    final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return "Bearer " + base64Encoder.encodeToString(randomBytes);
  }
}
