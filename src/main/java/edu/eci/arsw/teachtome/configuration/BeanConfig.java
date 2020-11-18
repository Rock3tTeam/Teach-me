package edu.eci.arsw.teachtome.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class BeanConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("rediscacheteachtome.redis.cache.windows.net", 6379);
        redisStandaloneConfiguration.setPassword(RedisPassword.of("a9v5Beu6tc2w64VYgueed99T+68UWQhMvFmH4mhLwJY="));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    public RedisCacheManager redisCacheManager(@Qualifier("jedisConnectionFactory") JedisConnectionFactory connectionFactory) {
        Map<String, RedisCacheConfiguration> cacheNamesConfigurationMap = new HashMap<>();
        cacheNamesConfigurationMap.put("filtered-classes-cache",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(900)));
        return new RedisCacheManager(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory),
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(300)),
                cacheNamesConfigurationMap);
    }
}
