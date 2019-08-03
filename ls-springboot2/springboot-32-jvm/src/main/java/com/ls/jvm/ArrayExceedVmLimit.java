package com.ls.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-23 14:22
 */
public class ArrayExceedVmLimit {
    public static void main(String[] args) {
        System.out.println();
        List<byte[]> list = new ArrayList<>();
        int i = 0;
        while (true) {
            list.add(new  byte[5*1024*1024]);
            System.out.println("count:"+(++i));
        }
    }

    static class Key {
        Integer id;
        public Key(Integer id) {
            this.id = id;
        }

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Key key = (Key) o;
//
//            return id != null ? id.equals(key.id) : key.id == null;
//        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }
}
