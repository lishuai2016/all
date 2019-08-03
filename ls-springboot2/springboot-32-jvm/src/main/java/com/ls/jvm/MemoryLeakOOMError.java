package com.ls.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-23 14:14
 */
public class MemoryLeakOOMError {
    public static void main(String[] args) {
        System.out.println();
       for (int i = 0;i<3;i++) {
           int[] arr = new int[Integer.MAX_VALUE - 1];
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
