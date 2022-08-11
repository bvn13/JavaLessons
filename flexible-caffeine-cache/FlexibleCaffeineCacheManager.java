package com.pf.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Flexible Caffeine cache manager
 * Idea: {@see "https://dzone.com/articles/multiple-cache-configurations-with-caffeine-and-sp"}
 *
 * to be used in cases when you need different settings per cache name
 *
 */
@Slf4j
@ConfigurationProperties(prefix = "cache")
public class FlexibleCaffeineCacheManager extends CaffeineCacheManager implements InitializingBean {

    /**
     * default <code>spring.cache.cache-names</code> settings
     * will be overridden with {@link FlexibleCaffeineCacheManager#specs}
     */
    @Value("${spring.cache.cache-names:}")
    private String springCacheNames;

    /**
     * default <code>spring.cache.caffeine.spec</code> settings
     * will be overridden with {@link FlexibleCaffeineCacheManager#specs}
     */
    @Value("${spring.cache.caffeine.spec:}")
    private String springCacheSpecs;

    /**
     * This field could be populated from <code>application.(yaml|properties)</code> file
     * All features are listed into {@link CaffeineSpec}
     */
    @Getter @Setter
    private Map<String, String> specs = new ConcurrentHashMap<>();
    @Getter
    private Map<String, Caffeine<Object, Object>> builders = new ConcurrentHashMap<>();

    private CacheLoader<Object, Object> cacheLoader;
    @Override
    public void setCacheLoader(@Nonnull CacheLoader<Object, Object> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    /**
     * reads all standard settings from application.(properties|yml) and adjust proper cache builders
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.hasText(springCacheNames) && !springCacheNames.equals("${spring.cache.cache-names:}")) {
            String[] cacheNames = springCacheNames.split(",");
            if (cacheNames.length > 0) {
                for (String cacheName : cacheNames) {
                    if (StringUtils.hasText(cacheName)) {
                        if (!specs.containsKey(cacheName.trim())) {
                            specs.put(cacheName.trim(), springCacheSpecs);
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, String> cacheSpecEntry : specs.entrySet()) {
            log.info("Creating cache with name {} and specs: {}", cacheSpecEntry.getKey(), cacheSpecEntry.getValue());
            builders.put(cacheSpecEntry.getKey(), Caffeine.from(cacheSpecEntry.getValue()));
        }
    }

    @Nonnull
    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        Caffeine<Object, Object> builder = builders.get(name);
        if (builder == null) {
            return super.createNativeCaffeineCache(name);
        }

        if (this.cacheLoader != null) {
            return builder.build(this.cacheLoader);
        } else {
            return builder.build();
        }
    }

}
