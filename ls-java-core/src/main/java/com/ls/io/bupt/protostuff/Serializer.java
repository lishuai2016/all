package com.ls.io.bupt.protostuff;

/**
 * Created by chenshunyang on 2016/11/18.
 */
public interface Serializer {
    /**
     * 序列化（对象 -> 字节数组）
     * @param obj
     * @param <T>
     * @return
     */
     <T> byte[] serialize(T obj);

    /**
     * 反序列化（字节数组 -> 对象）
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
     <T> Object deserialize(byte[] bytes, Class<T> clazz);
}
