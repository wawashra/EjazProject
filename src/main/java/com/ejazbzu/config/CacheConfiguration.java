package com.ejazbzu.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.ejazbzu.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.ejazbzu.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.ejazbzu.domain.User.class.getName());
            createCache(cm, com.ejazbzu.domain.Authority.class.getName());
            createCache(cm, com.ejazbzu.domain.User.class.getName() + ".authorities");
            createCache(cm, com.ejazbzu.domain.University.class.getName());
            createCache(cm, com.ejazbzu.domain.University.class.getName() + ".colleges");
            createCache(cm, com.ejazbzu.domain.College.class.getName());
            createCache(cm, com.ejazbzu.domain.College.class.getName() + ".departments");
            createCache(cm, com.ejazbzu.domain.Department.class.getName());
            createCache(cm, com.ejazbzu.domain.Department.class.getName() + ".courses");
            createCache(cm, com.ejazbzu.domain.Course.class.getName());
            createCache(cm, com.ejazbzu.domain.Course.class.getName() + ".documents");
            createCache(cm, com.ejazbzu.domain.Course.class.getName() + ".students");
            createCache(cm, com.ejazbzu.domain.Document.class.getName());
            createCache(cm, com.ejazbzu.domain.Document.class.getName() + ".attachments");
            createCache(cm, com.ejazbzu.domain.Document.class.getName() + ".reports");
            createCache(cm, com.ejazbzu.domain.Document.class.getName() + ".tags");
            createCache(cm, com.ejazbzu.domain.AttachmentType.class.getName());
            createCache(cm, com.ejazbzu.domain.AttachmentType.class.getName() + ".attachments");
            createCache(cm, com.ejazbzu.domain.Attachment.class.getName());
            createCache(cm, com.ejazbzu.domain.Tag.class.getName());
            createCache(cm, com.ejazbzu.domain.Tag.class.getName() + ".documents");
            createCache(cm, com.ejazbzu.domain.Report.class.getName());
            createCache(cm, com.ejazbzu.domain.Student.class.getName());
            createCache(cm, com.ejazbzu.domain.Student.class.getName() + ".documents");
            createCache(cm, com.ejazbzu.domain.Student.class.getName() + ".reports");
            createCache(cm, com.ejazbzu.domain.Student.class.getName() + ".courses");
            createCache(cm, com.ejazbzu.domain.EntityAuditEvent.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache == null) {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

}
