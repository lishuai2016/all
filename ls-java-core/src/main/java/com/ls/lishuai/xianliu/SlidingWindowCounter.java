package com.ls.lishuai.xianliu;//package ls.xianliu;
//
///**
// * @Author: lishuai
// * @CreateDate: 2018/8/3 10:41
// */
//public class SlidingWindowCounter {
//    private volatile SlotBaseCounter slotBaseCounter;
//    private volatile int windowSize;
//    private volatile int head;
//
//    public SlidingWindowCounter(int windowSize) {
//        resizeWindow(windowSize);
//    }
//
//    public synchronized void resizeWindow(int windowSize) {
//        this.windowSize = windowSize;
//        this.slotBaseCounter = new SlotBaseCounter(windowSize);
//        this.head = 0;
//    }
//
//    public void increase() {
//        slotBaseCounter.increaseSlot(head);
//    }
//
//    public int totalAndAdvance() {
//        int total = totalCount();
//        advance();
//        return total;
//    }
//
//    public void advance() {
//        int tail = (head + 1) % windowSize;
//        slotBaseCounter.wipeSlot(tail);
//        head = tail;
//    }
//
//    public int totalCount() {
//        return slotBaseCounter.totalCount();
//    }
//
//    @Override
//    public String toString() {
//        return "total = " + totalCount() + " head = " + head + " >> " + slotBaseCounter;
//    }
//}
