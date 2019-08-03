package com.ls.common.sys.service.impl;

import com.ls.common.sys.entity.Course;
import com.ls.common.sys.mapper.CourseMapper;
import com.ls.common.sys.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2018-12-14
 * 备注：
 * 不在service上添加名称会出现两个接口的实现ICourseService，启动报错
 * @Service("iCourseService")
 *
 */
@Service("iCourseService")
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {


}
