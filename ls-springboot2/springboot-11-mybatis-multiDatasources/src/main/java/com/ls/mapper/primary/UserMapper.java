package com.ls.mapper.primary;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 19:40
 */
@Repository
public interface UserMapper {
    List<UserMapper> selectUserList();
}
