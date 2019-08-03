package com.ls.li.knn;

import java.util.Comparator;

/**
 * Created by lishuai on 2018/2/14.
 */
public class CompareClass implements Comparator<Distance> {
    @Override
    public int compare(Distance d1, Distance d2) {
        return d1.getDisatance()>d2.getDisatance()?20 : -1;
    }
}
