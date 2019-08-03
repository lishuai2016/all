package com.ls.design_pattern.Adapter;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 21:06
 */
public class WildTurkey implements Turkey {
    @Override
    public void gobble() {
        System.out.println("gobble gobble");
    }
    @Override
    public void fly() {
        System.out.println("i am flying a short distance");
    }

}
