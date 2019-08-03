package com.ls.mini.spring.ioc.beans.factory;

import com.ls.mini.spring.ioc.aop.BeanFactoryAware;
import com.ls.mini.spring.ioc.beans.BeanDefinition;
import com.ls.mini.spring.ioc.BeanReference;
import com.ls.mini.spring.ioc.beans.PropertyValue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @program: ls-mini-spring
 * @author: lishuai
 * @create: 2019-07-21 14:14
可自动装配内容的BeanFactory
 */
public class AutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {
//        for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
//            Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
//            declaredField.setAccessible(true);
//            Object value = propertyValue.getValue();
//            if (value instanceof BeanReference) {
//                BeanReference beanReference = (BeanReference) value;
//                value = getBean(beanReference.getName());//获取ref实例，这里会不会产生循环依赖？？？
//            }
//            declaredField.set(bean, value);
//        }

        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        for (PropertyValue propertyValue : mbd.getPropertyValues().getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }

            try {
                Method declaredMethod = bean.getClass().getDeclaredMethod(
                        "set" + propertyValue.getName().substring(0, 1).toUpperCase()
                                + propertyValue.getName().substring(1), value.getClass());
                declaredMethod.setAccessible(true);

                declaredMethod.invoke(bean, value);
            } catch (NoSuchMethodException e) {
                Field declaredField = bean.getClass().getDeclaredField(propertyValue.getName());
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }

}
