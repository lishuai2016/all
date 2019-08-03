package com.ls.lishuai.java.annotation;

/**
 * @Author: lishuai
 * @CreateDate: 2018/6/6 20:41
 */
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 注解处理器
 */
public class FruitInfoUtil {
    public static void getFruitInfo(Class<?> clazz){

        String strFruitName=" 水果名称：";
        String strFruitColor=" 水果颜色：";
        String strFruitProvicer="供应商信息：";
        String strfruitAction = "执行的操作：";

        Field[] fields = clazz.getDeclaredFields();


        for(Field field :fields){
            if(field.isAnnotationPresent(FruitName.class)){
                FruitName fruitName = (FruitName) field.getAnnotation(FruitName.class);
                strFruitName=strFruitName+fruitName.value();
                System.out.println(strFruitName);
            }
            else if(field.isAnnotationPresent(FruitColor.class)){
                FruitColor fruitColor= (FruitColor) field.getAnnotation(FruitColor.class);
                strFruitColor=strFruitColor+fruitColor.fruitColor().toString();
                System.out.println(strFruitColor);
            }
            else if(field.isAnnotationPresent(FruitProvider.class)){
                FruitProvider fruitProvider= (FruitProvider) field.getAnnotation(FruitProvider.class);
                strFruitProvicer=" 供应商编号："+fruitProvider.id()+" 供应商名称："+fruitProvider.name()+" 供应商地址："+fruitProvider.address();
                System.out.println(strFruitProvicer);
            }
        }


        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method m: declaredMethods) {
            if (m.isAnnotationPresent(FruitAction.class)) {
                FruitAction fruitAction = m.getAnnotation(FruitAction.class);
                strfruitAction=fruitAction.value();
                System.out.println(strfruitAction);
                Annotation[][] mParameterAnnotations = m.getParameterAnnotations();
                for(int i = 0;i < mParameterAnnotations.length;i++){
                    System.out.println("annotations[i].length");
                    for(int j = 0;j < mParameterAnnotations[i].length;j++){
                        System.out.println(mParameterAnnotations[i][j]);
                    }
                }

                System.out.println("1111111111111");


            }
        }

    }
}
