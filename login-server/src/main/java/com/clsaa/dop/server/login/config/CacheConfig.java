package com.clsaa.dop.server.login.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 缓存配置
 *
 * @author joyren
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    /**
     * 缓存名，名称暗示了缓存时长
     * 注意： 如果添加了新的缓存名，需要同时在下面的RedisCacheCustomizer#RedisCacheCustomizer里配置名称对应的缓存时长，时长为0代表永不过期
     *
     * @see CacheConfig#cacheManager(RedisTemplate)
     */
    public interface CacheNames {
        /**
         * 15分钟缓存组
         */
        String CACHE_EXPIRED_15_MINS = "dop:login:cache:15m";
        /**
         * 30分钟缓存组
         */
        String CACHE_EXPIRED_30_MINS = "dop:login:cache:30m";
        /**
         * 60分钟缓存组
         */
        String CACHE_EXPIRED_60_MINS = "dop:login:cache:60m";
        /**
         * 180分钟缓存组
         */
        String CACHE_EXPIRED_180_MINS = "dop:login:cache:180m";

    }

    public class Constants {

        /**
         * 默认日期时间格式
         */
        static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        /**
         * 默认日期格式
         */
        static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
        /**
         * 默认时间格式
         */
        static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    }

    /**
     * 配置CacheManager
     */
    @Bean
    @SuppressWarnings("unchecked")
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        // RedisCache需要一个RedisCacheWriter来实现读写Redis
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        // SerializationPair用于Java对象和Redis之间的序列化和反序列化
        // Spring Boot默认采用JdkSerializationRedisSerializer的二进制数据序列化方式
        // 使用该方式，保存在redis中的值是人类无法阅读的乱码，并且该Serializer要求目标类必须实现Serializable接口
        // 本项目使用StringRedisSerializer来序列化和反序列化redis的key值
        RedisSerializationContext.SerializationPair keySerializationPair = RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer());
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_DATE_TIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_DATE_FORMAT)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(JacksonConfig.Constants.DEFAULT_TIME_FORMAT)));
        om.registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(javaTimeModule);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        RedisSerializationContext.SerializationPair valueSerializationPair = RedisSerializationContext.SerializationPair
                .fromSerializer(jackson2JsonRedisSerializer);
        // 构造一个RedisCache的配置对象，设置缓存过期时间和Key、Value的序列化机制
        // 设置默认缓存过期时间为30s
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(30))
                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair);
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = ImmutableMap.<String, RedisCacheConfiguration>builder()
                .put(CacheNames.CACHE_EXPIRED_15_MINS,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(15))
                                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair))
                .put(CacheNames.CACHE_EXPIRED_30_MINS,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(30))
                                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair))
                .put(CacheNames.CACHE_EXPIRED_60_MINS,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(60))
                                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair))
                .put(CacheNames.CACHE_EXPIRED_180_MINS,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(180))
                                .serializeKeysWith(keySerializationPair).serializeValuesWith(valueSerializationPair))
                .build();
        return new RedisCacheManager(writer, config, redisCacheConfigurationMap);
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    static class LoggingCacheErrorHandler extends SimpleCacheErrorHandler {
        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheGetError(exception, cache, key);
        }

        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
            logger.error(String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCachePutError(exception, cache, key, value);
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
            logger.error(String.format("cacheName:%s,cacheKey:%s", cache == null ? "unknown" : cache.getName(), key), exception);
            super.handleCacheEvictError(exception, cache, key);
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            logger.error(String.format("cacheName:%s", cache == null ? "unknown" : cache.getName()), exception);
            super.handleCacheClearError(exception, cache);
        }
    }
}
