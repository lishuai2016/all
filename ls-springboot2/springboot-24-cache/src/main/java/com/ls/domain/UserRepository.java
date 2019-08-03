package com.ls.domain;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;



/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16
 */
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(key = "#p0", condition = "#p0.length() < 10")
    User findByName(String name);

}
