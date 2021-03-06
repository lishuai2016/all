package com.ls.design_pattern.ChainOfResponsibility.chain;

/**
 * 销售总监，可以批准 40% 以内的折扣
 * <p>
 * Created by cipher on 2017/9/12.
 */
public class Director extends PriceHandler {

    public void processDiscount(float discount) {
        if (discount <= 0.4f) {
            System.out.format("%s批准了折扣：%.2f%n", this.getClass().getName(), discount);
        } else {
            this.getSuccessor().processDiscount(discount);
        }
    }

}
