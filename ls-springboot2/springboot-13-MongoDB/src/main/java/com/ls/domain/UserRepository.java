package com.ls.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-16 13:25
 */
public interface UserRepository extends MongoRepository<User, Long> {

    User findByUsername(String username);

}
