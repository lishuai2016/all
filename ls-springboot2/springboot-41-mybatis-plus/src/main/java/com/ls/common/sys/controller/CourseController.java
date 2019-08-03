package com.ls.common.sys.controller;


import com.ls.common.sys.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2018-12-14
 */
@RestController
@RequestMapping("/sys/course")
public class CourseController {

    @Autowired
    private ICourseService iCourseService;

    @GetMapping("/index")
    public Object index() {
        return iCourseService.list();
    }

}
