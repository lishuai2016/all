package com.ls.design_pattern.Prototype;

/**
 * @program: lishuai-notes
 * @author: lishuai
 * @create: 2018-12-03 23:44
 */
public class ConcretePrototype extends Prototype {
    private String filed;
    public ConcretePrototype(String filed) {
        this.filed = filed;
    }
    @Override
    Prototype myclone() {
        return new ConcretePrototype(this.filed);
    }

    @Override
    public String toString() {
        return filed;
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }
}
