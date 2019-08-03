package com.ls.design_pattern.Strategy.handler_demo.service.impl;


import com.ls.design_pattern.Strategy.handler_demo.handler.AbstractHandler;
import com.ls.design_pattern.Strategy.handler_demo.handler.HandlerContext;
import com.ls.design_pattern.Strategy.handler_demo.model.OrderDTO;
import com.ls.design_pattern.Strategy.handler_demo.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: CipherCui
 * @Description:
 * @Date: Created in 9:54 2019/2/2
 */
@Service
public class OrderServiceV2Impl implements IOrderService {

    @Autowired
    private HandlerContext handlerContext;  //这个类比策略模式中的策略接口

    @Override
    public String handle(OrderDTO dto) {
        AbstractHandler handler = handlerContext.getInstance(dto.getType());
        return handler.handle(dto);
    }

}
