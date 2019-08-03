package com.ls.design_pattern.Strategy.handler_demo.handler.biz;


import com.ls.design_pattern.Strategy.handler_demo.handler.AbstractHandler;
import com.ls.design_pattern.Strategy.handler_demo.handler.HandlerType;
import com.ls.design_pattern.Strategy.handler_demo.model.OrderDTO;
import org.springframework.stereotype.Component;

/**
 * @Author: CipherCui
 * @Description: 促销订单处理器
 * @Date: Created in 10:17 2019/2/2
 */
@Component
@HandlerType("3")
public class PromotionHandler extends AbstractHandler {

    @Override
    public String handle(OrderDTO dto) {
        return "处理促销订单";
    }

}
