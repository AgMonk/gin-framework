package config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;


/**
 * Redis配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/4/16 15:46
 **/
@Configuration
@AllArgsConstructor
@EnableCaching
@Slf4j
public class RedisConfig {
    public static final GenericJackson2JsonRedisSerializer GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER = getSerializer();
    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";

    private static GenericJackson2JsonRedisSerializer getSerializer() {
        final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .featuresToEnable(
                        //反序列化时 空串识别为 null
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
                ).featuresToDisable(
                        // 反序列化时,遇到未知属性会不会报错
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        MapperFeature.USE_ANNOTATIONS
                ).modules(
                        //支持 ZonedDateTime
                        new JavaTimeModule()
                )

                .build();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> jsonTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateBuilder<String, Object>(redisConnectionFactory)
                .setValueSerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER)
                .setHashValueSerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER)
                .build();
    }

    @Bean(name = RedisConfig.REDIS_CACHE_MANAGER)
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .computePrefixWith(cacheName -> "Cache:" + cacheName + ":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GENERIC_JACKSON_2_JSON_REDIS_SERIALIZER));

        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(config).transactionAware().build();
    }

    @Bean
    public RedisTemplate<String, String> stringTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateBuilder<String, String>(redisConnectionFactory).setValueSerializer(new StringRedisSerializer()).setHashValueSerializer(
                new StringRedisSerializer()).build();
    }

    /**
     * RedisTemplate 构造器
     * key的序列化方式默认为string
     * @param <K> Key类型
     * @param <V> Value类型
     */
    public static class RedisTemplateBuilder<K, V> {
        private final RedisTemplate<K, V> template = new RedisTemplate<>();

        public RedisTemplateBuilder(RedisConnectionFactory redisConnectionFactory) {
            template.setConnectionFactory(redisConnectionFactory);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setKeySerializer(new StringRedisSerializer());
        }

        public RedisTemplate<K, V> build() {
            template.afterPropertiesSet();
            return template;
        }

        public RedisTemplateBuilder<K, V> setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
            template.setHashKeySerializer(hashKeySerializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
            template.setHashValueSerializer(hashValueSerializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setKeySerializer(RedisSerializer<?> serializer) {
            template.setKeySerializer(serializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setValueSerializer(RedisSerializer<?> serializer) {
            template.setValueSerializer(serializer);
            return this;
        }
    }

}
