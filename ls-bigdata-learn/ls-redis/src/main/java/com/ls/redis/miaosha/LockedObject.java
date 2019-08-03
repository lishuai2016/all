package com.ls.redis.miaosha;

import java.lang.annotation.*;

/**
 * 
 * @author liushao
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LockedObject {
	

}
