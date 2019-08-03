package com.ls.design_pattern.Builder.example;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:17
 */
public class StringBuilder extends AbstractStringBuilder {
    public StringBuilder() {
        super(16);
    }

    @Override
    public String toString() {
        // Create a copy, don't share the array
        return new String(value, 0, count);
    }
}
