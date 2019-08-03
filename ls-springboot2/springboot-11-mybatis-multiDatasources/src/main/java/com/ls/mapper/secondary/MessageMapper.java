package com.ls.mapper.secondary;

import com.ls.domain.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-15 19:40
 */
@Repository
public interface MessageMapper {
    List<Message> selectMessageList();
}
