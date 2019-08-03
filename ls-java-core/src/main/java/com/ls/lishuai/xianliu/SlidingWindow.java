package com.ls.lishuai.xianliu;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: lishuai
 * @CreateDate: 2018/8/2 20:34
 *
 * https://www.cnblogs.com/clds/p/5850008.html
 * http://go12345.iteye.com/blog/1744728
 *
 */
public class SlidingWindow {
    /* 循环队列 */
    private volatile AtomicInteger[] timeSlices;
    /* 队列的总长度  */
    private volatile int timeSliceSize;
    /* 每个时间片的时长 */
    private volatile int timeMillisPerSlice;
    /* 窗口长度 */
    private volatile int windowSize;

    /* 当前所使用的时间片位置 */
    private AtomicInteger cursor = new AtomicInteger(0);

    public SlidingWindow(int timeMillisPerSlice, int windowSize) {
        this.timeMillisPerSlice = timeMillisPerSlice;
        this.windowSize = windowSize;
        // 保证存储在至少两个window
        this.timeSliceSize = windowSize * 2 + 1;
    }

    /**
     * 初始化队列，由于此初始化会申请一些内容空间，为了节省空间，延迟初始化
     */
    private void initTimeSlices() {
        if (timeSlices != null) {
            return;
        }
        // 在多线程的情况下，会出现多次初始化的情况，没关系
        // 我们只需要保证，获取到的值一定是一个稳定的，所有这里使用先初始化，最后赋值的方法
        AtomicInteger[] localTimeSlices = new AtomicInteger[timeSliceSize];
        for (int i = 0; i < timeSliceSize; i++) {
            localTimeSlices[i] = new AtomicInteger(0);
        }
        timeSlices = localTimeSlices;
    }

    private int locationIndex() {
        long time = System.currentTimeMillis();
        return (int) ((time / timeMillisPerSlice) % timeSliceSize);
    }

    /**
     * <p>对时间片计数+1，并返回窗口中所有的计数总和
     * <p>该方法只要调用就一定会对某个时间片进行+1
     *
     * @return
     */
    public int incrementAndSum() {
        initTimeSlices();
        int index = locationIndex();
        int sum = 0;
        // cursor等于index，返回true
        // cursor不等于index，返回false，并会将cursor设置为index
        int oldCursor = cursor.getAndSet(index);
        if (oldCursor == index) {
            // 在当前时间片里继续+1
            sum += timeSlices[index].incrementAndGet();
        } else {
            // 可能有其他thread已经置过1，问题不大
            timeSlices[index].set(1);
            // 清零，访问量不大时会有时间片跳跃的情况
            clearBetween(oldCursor, index);
            // sum += 0;
        }
        for (int i = 1; i < windowSize; i++) {
            sum += timeSlices[(index - i + timeSliceSize) % timeSliceSize].get();
        }
        return sum;
    }

    /**
     * 判断是否允许进行访问，未超过阈值的话才会对某个时间片+1
     *
     * @param threshold
     * @return
     */
    public boolean allow(int threshold) {
        initTimeSlices();
        int index = locationIndex();
        int sum = 0;
        // cursor不等于index，将cursor设置为index
        int oldCursor = cursor.getAndSet(index);
        if (oldCursor != index) {
            // 可能有其他thread已经置过1，问题不大
            timeSlices[index].set(0);
            // 清零，访问量不大时会有时间片跳跃的情况
            clearBetween(oldCursor, index);
        }
        for (int i = 1; i < windowSize; i++) {
            sum += timeSlices[(index - i + timeSliceSize) % timeSliceSize].get();
        }

        // 阈值判断
        if (sum <= threshold) {
            // 未超过阈值才+1
            sum += timeSlices[index].incrementAndGet();
            return true;
        }
        return false;
    }

    /**
     * <p>将fromIndex~toIndex之间的时间片计数都清零
     * <p>极端情况下，当循环队列已经走了超过1个timeSliceSize以上，这里的清零并不能如期望的进行
     *
     * @param fromIndex 不包含
     * @param toIndex 不包含
     */
    private void clearBetween(int fromIndex, int toIndex) {
        for (int index = (fromIndex + 1) % timeSliceSize; index != toIndex; index = (index + 1) % timeSliceSize) {
            timeSlices[index].set(0);
        }
    }
}
